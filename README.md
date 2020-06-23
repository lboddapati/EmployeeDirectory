# Build Tools & Versions Used
- Android Studio: 4.0
- Build system: Gradle 6.1.1
- Android API level supported: 21+

# Focus Areas
I focused on creating a clean, modular and testable architecture incorporating best practices. See more under [Architecture](#architecture).

# Copied-in code / dependencies
I used a pre-written template for the base architecture. All this code was still written by me previously. I use this template as a starting point for my other sample projects. Everything else was written specifically for this project. The pre-written template includes:
- Everything in the [base](app/src/main/java/com/interview/employeedirectory/base) package (with application specific modifications to KoinModules.kt and DirectoryApplication.kt)
- Skeleton code for the files in [datalayer](app/src/main/java/com/interview/employeedirectory/datalayer) package.
- The unit test setup under the [base](app/src/test/java/com/interview/employeedirectory/base) package.

# Tablet / phone focus
The application UI was built to be optimal on a phone. However, it should still scale relatively decently to a tablet.

# How long you spent on the project
Roughly 5-6 hours.

# Implementation Details
## Architecture
### MVP:
The app is built using MVP architecture pattern.

- Model - This is essentially the Data Layer. The DataRepository is used to fetch/update data (remote or local).
- View - Implemented by Activity (or Fragment). Renders UI elements. See [EmployeeListActivity.kt](app/src/main/java/com/interview/employeedirectory/ui/employeelist/EmployeeListActivity.kt).
- Presenter - Contains Business logic. Interacts with the Data Layer and View. See [EmployeeListPresenter.kt](app/src/main/java/com/interview/employeedirectory/ui/employeelist/EmployeeListPresenter.kt).

### [Data Layer](app/src/main/java/com/interview/employeedirectory/datalayer):
- DataRepository - The implementation details of the Data Layer are abstracted away by the DataRepository interface.
- ApiService (Network Layer) - Retrofit + RxJava + Moshi are used for working with Network Layer.
- **TODO** - Implement a CacheRepository for (optionally) caching network responses.
- [LifecycleAwareSubscriptionManager](app/src/main/java/com/interview/employeedirectory/base/LifecycleAwareSubscriptionManager.kt) - Manages Rx subscriptions based on Android Lifecycle events to prevent potential memory leaks caused by subscriptions running beyond the Activity lifecycle.

### Caching:
- **Network response** - Ideally, I would implement an on disk CacheRepository and attempt to load from cache before hitting the network. However, a full cache implementation (with proper cache invalidation and expiration) was taking longer than I anticipated. I settled for storing the employee list in a saved instance state for now (in the interest of time) although it comes with some [caveats](#assumptions--caveats--limitations).
- **Images** - I chose Glide for image loading because it comes with a built in on-disk caching capabilities for images. Glide saves several versions of the same image (one for each size that is used in the app) which makes it both memory-efficient and also fast in loading images.

### Dependency Injection:
The application uses Koin for injecting the DataRepository and Presenters. See [KoinModules.kt](app/src/main/java/com/interview/employeedirectory/base/KoinModules.kt)

## Libraries used
- Networking: [Retrofit](https://github.com/square/retrofit), [RxJava](https://github.com/ReactiveX/RxJava), [RxAndroid](https://github.com/ReactiveX/RxAndroid), [Moshi](https://github.com/square/moshi)
- Image Loading: [Glide](https://github.com/bumptech/glide)
- Dependency Injection: [Koin](https://github.com/InsertKoinIO/koin)
- Testing: koin-test, [mockito-kotlin](https://github.com/nhaarman/mockito-kotlin), [Robolectric](https://github.com/robolectric/robolectric)

## Assumptions / Caveats / Limitations
- The employee list screen is built based on the assumption that the response returned from server is small. The current implementation might not work well for large list of employee objects.
  - **TODO (performance improvements)**:
    - Implement paginated + endless scrolling for performance improvements.
    - Implement disk caching for employee list request to make subsequent requests faster. Currently the employee list is saved in instance state to avoid re-fetching the data upon device configuration changes (such as rotation). However, for large data, this would throw a TransactionTooLargeException.

## Known Issues
- The LifecycleAwareSubscriptionManager disposes of any inflight requests when it receives an ON_STOP lifecycle event. As a result, if the user navigates away from the app (backgrounds the app for example) while the employee list is still loading and comes back to it, they might see a forever loading spinner.
  - **TODO (potential fixes)**:
    - Build capability into subscription manager to replay inflight requests that were discarded.
    - Refresh employee list when user comes back to the activity i.e load requests in onResume instead of onCreate.

# Demo
![demo](https://media.giphy.com/media/TGLjKOX75kiruaV4gE/giphy.gif)
