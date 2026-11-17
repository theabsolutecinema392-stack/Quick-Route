# Quick Route — Android starter

Quick Route is a single Android app with four demo roles: Customer, Vendor, Delivery Partner and Admin.

## Build
Requires JDK 17, Android SDK platform 35 and Gradle 8.9.

```bash
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

GitHub Actions workflow: `.github/workflows/android.yml`. Push this project to a GitHub repository and run **Actions → Build Quick Route APK**. The APK is uploaded as a workflow artifact.

## Current prototype scope
- Role switching for Customer, Vendor, Delivery Partner and Admin.
- Sample food menu and in-memory demo orders.
- Vendor and delivery-partner application forms.
- Demo order acceptance, pickup and delivery status.
- Payment-method selection: UPI, Card/Net Banking, Cash on Delivery.

## Before a real public launch
This is a UI/logic prototype, not a production delivery platform. Data currently resets when the app process is restarted. Real users, authentication, vendor verification, location permissions, maps/live tracking, push notifications, server-side order management, payouts, refunds and secure payment gateway integration still need a backend and testing. Payment selection here does **not** charge money. Never put payment secrets in the Android app.
