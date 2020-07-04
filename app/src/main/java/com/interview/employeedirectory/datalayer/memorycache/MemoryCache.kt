package com.interview.employeedirectory.datalayer.memorycache

import io.reactivex.Maybe
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.concurrent.TimeUnit

private const val DEFAULT_TTL_MINS = 2L
data class MemoryCache<T>(
    private val cache: HashMap<ParameterCacheKey, CacheData<T>> = hashMapOf(),
    private val timeToLive: Long =  TimeUnit.MINUTES.toMillis(DEFAULT_TTL_MINS)
) {
    fun get(vararg params: Any): Maybe<T> {
        val cacheHit = cache[ParameterCacheKey(params)]
        return if (cacheHit == null || cacheHit.isExpired()) {
            Maybe.empty()
        } else {
            Maybe.just(cacheHit.data)
        }
    }

    fun add(data: T, vararg params: Any) {
        cache[ParameterCacheKey(params)] = CacheData(data = data, timeToLive = timeToLive)
    }

    fun removeExpired() = cache.entries
        .filter { it.value.isExpired() }
        .forEach { cache.remove(it.key) }
}

class ParameterCacheKey(vararg params: Any) {

    private val mParams: Array<Any> = arrayOf(params)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is ParameterCacheKey) return false

        return EqualsBuilder().append(this.mParams, other.mParams).build()
    }

    override fun hashCode() = HashCodeBuilder().append(mParams).toHashCode()
}

data class CacheData<T>(
    val data: T,
    private val timeToLive: Long,
    private val timeCreated: Long = System.currentTimeMillis()
) {
    fun isExpired() = System.currentTimeMillis() - timeCreated > timeToLive
}