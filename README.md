# Tilt Greeting Assistant for Android

Android project for inspecting the visible Tilt live-room UI and preparing a user-controlled greeting workflow.

## Current build

The APK currently runs in **inspection mode**. It uses Android AccessibilityService to capture the visible Tilt accessibility tree so the real Android controls can be identified from the user's device. It does not type or post chat messages in this build.

This is intentional: the exact Tilt Android UI selectors need to be confirmed on the user's installed app before adding optional UI actions.

## Free build

GitHub Actions builds a debug APK automatically on pushes to `main`, and it can also be started manually with **Actions → Build Android APK → Run workflow**.

The APK is uploaded as the `tilt-greeting-assistant-debug` artifact when the build succeeds.

## Important limitations

- This is UI automation through Android's normal accessibility framework, not a private Tilt API.
- It must not bypass login, Cloudflare/Turnstile, rate limits, anti-bot controls, or other security measures.
- Automated/repetitive chat may violate a platform's rules and can result in restrictions. Use only where permitted.
- Keep greeting frequency low and use deduplication/cooldowns when an action-enabled version is used.

## Next step

Install this APK, enable the service, open the exact Tilt live-room screen, wait 5–10 seconds, then return to the app and copy the captured UI report. That report lets the project be tuned to the actual viewer list and chat input controls rather than guessing selectors.
