# Call App

## Overview

The Call App is a simple yet powerful application that enables users to make video calls using the
Agora platform. It follows a clean and efficient code structure with MVVM architecture, Jetpack
Compose for UI, and Dagger Hilt for dependency injection.

## Features

- **Seamless Video Calls:** Make video calls effortlessly using Agora (Currently in progress).

- **Secure Authentication:** Log in securely with Firebase Auth.

- **Streamlined Development:**
    - **MVVM Architecture:** Structured for simplicity and efficiency.
    - **Jetpack Compose:** Intuitive user interface.
    - **Dagger Hilt:** Smooth dependency injection.
    - **Retrofit:** Easy networking.
    - **Kotlin Coroutines and Flow:** Asynchronous programming.
    - **Room:** Local database management.

## Getting Started

1. Clone the repository: `git clone https://github.com/Deuspheara/CallApp.git`
2. Open the project in your IDE.
3. Configure Firebase for authentication/firestore.
4. Build and run the app on your device or emulator.

### Agora Setup

To enable video calls, you need to set up Agora. Follow these steps:

1. Sign up for an Agora account at [Agora Console](https://console.agora.io/).
2. Create a new project in the Agora Console and obtain your App ID.
3. Open the `local.properties` file in your project and add the following lines:

```properties
AGORA_APP_ID="your_agora_app_id"
AGORA_SELF_HOSTED_URL="your_self_hosted_url_if_any"
```

Replace `"your_agora_app_id"` with your Agora App ID and `"your_self_hosted_url_if_any"` with your
self-hosted Agora URL if applicable.

Now, you're all set to enjoy video calls using Agora in the Call App!

## What's Next

We're continuously improving the Call App. Expect:

- **Enhanced User Feedback:** Improved feedback mechanisms during video calls.

- **Progress Indicators:** Clearer indicators for upcoming features.

- **Error Handling:** Robust error handling for a trouble-free experience.
