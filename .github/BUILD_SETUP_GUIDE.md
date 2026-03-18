# ⚡ GitHub Actions Build Setup Guide

## 🎯 What This Does

Every time you push to GitHub, the workflow will:
1. ✅ Build your APK automatically (~3 minutes)
2. 📦 Upload APK as downloadable artifact
3. 📧 Send email to **lobinpek2@gmail.com** on success or failure
4. 📊 Generate build summary in GitHub Actions

---

## 🔐 STEP 1: Configure Email Secrets (REQUIRED)

### Go to GitHub Secrets
1. Open: **https://github.com/lobbbin/cssim/settings/secrets/actions**
2. Click **"New repository secret"**

### Add EMAIL_USERNAME Secret
```
Name:  EMAIL_USERNAME
Value: lobinpek2@gmail.com
```

### Add EMAIL_PASSWORD Secret

**⚠️ IMPORTANT:** You need a Gmail App Password, NOT your regular password!

#### How to Create Gmail App Password:

1. **Go to Google Account Security**
   - https://myaccount.google.com/security

2. **Enable 2-Step Verification** (if not already enabled)
   - Must be enabled to create app passwords

3. **Go to App Passwords**
   - https://myaccount.google.com/apppasswords

4. **Create New App Password**
   - Select app: **Mail**
   - Select device: **Other (Custom name)**
   - Enter: `GitHub Actions`
   - Click **Generate**

5. **Copy the 16-character password**
   - Looks like: `abcd efgh ijkl mnop`
   - Remove spaces: `abcdefghijklmn`
   - **Save this securely!**

6. **Add to GitHub Secrets**
```
Name:  EMAIL_PASSWORD
Value: [your-16-character-password-without-spaces]
```

---

## ✅ STEP 2: Verify Workflow is Active

1. Go to: **https://github.com/lobbbin/cssim/actions**
2. You should see: **"Android APK Build"** workflow
3. It should be enabled (green checkmark)

---

## 🚀 STEP 3: Trigger a Build

### Option A: Push a Commit
```bash
# Make any change and push
git add .
git commit -m "Test build"
git push origin main
```

### Option B: Manual Trigger
1. Go to: **https://github.com/lobbbin/cssim/actions/workflows/android-build.yml**
2. Click **"Run workflow"** (right side)
3. Select branch: **main**
4. Click **"Run workflow"**

---

## 📧 Email Notifications

### On Build Success ✅

**Subject:** `✅ Build Successful - Country Simulator (cssim) - [DATE]`

**Contains:**
- Build information
- APK download link
- Installation instructions

### On Build Failure ❌

**Subject:** `❌ BUILD FAILED - Country Simulator (cssim) - [DATE]`

**Contains:**
- Error details
- Link to workflow logs
- Troubleshooting steps

---

## 📦 Downloading the APK

### After Successful Build:

1. **Go to the workflow run**
   - https://github.com/lobbbin/cssim/actions

2. **Click on the latest run** (top of the list)

3. **Scroll to "Artifacts" section** (bottom of the page)

4. **Click on the APK artifact**
   - Name: `CountrySimulator-Debug-[DATE]`

5. **Download the APK**
   - File: `app-debug.apk`
   - Size: ~50-100 MB

6. **Install on Android device**
   - Transfer APK to device
   - Enable "Install from Unknown Sources"
   - Install and play!

---

## ⚡ Build Optimization (3 Minute Target)

The workflow is optimized for speed:

| Optimization | Benefit |
|--------------|---------|
| **Gradle Caching** | Reuses dependencies from previous builds |
| **Parallel Builds** | Compiles multiple modules simultaneously |
| **Incremental Disabled** | Faster clean builds |
| **JVM Args Optimized** | More memory for faster compilation |
| **Single Fetch Depth** | Faster checkout (only latest commit) |

**Expected Build Times:**
- First build: ~5-7 minutes
- Cached builds: ~2-3 minutes

---

## 🔧 Troubleshooting

### Build Fails with "No Space Left on Device"
```yaml
# Add to android-build.yml under build step:
- name: 🧹 Free Up Space
  run: |
    sudo rm -rf /usr/share/dotnet
    sudo rm -rf /opt/ghc
    sudo rm -rf "/usr/local/share/boost"
```

### Email Not Sending

1. **Check secrets are set correctly**
   - Go to: https://github.com/lobbbin/cssim/settings/secrets/actions
   - Verify both `EMAIL_USERNAME` and `EMAIL_PASSWORD` exist

2. **Verify Gmail App Password**
   - Make sure you used App Password, not regular password
   - App password is 16 characters (no spaces)

3. **Check 2-Step Verification**
   - Must be enabled for app passwords to work
   - https://myaccount.google.com/security

### APK Not in Artifacts

1. **Check build succeeded**
   - Look for green checkmark in Actions tab

2. **Check artifact retention**
   - Artifacts expire after 30 days
   - Download immediately after build

---

## 📊 Build Summary

After each build, check the summary:

1. Go to: https://github.com/lobbbin/cssim/actions
2. Click on the latest run
3. Scroll to bottom for **"Build Summary"**

**Shows:**
- Repository info
- Commit hash
- Build date/time
- APK size
- Download link

---

## 🎯 Workflow Triggers

The build runs automatically on:

- ✅ Push to `main` branch
- ✅ Push to `develop` branch
- ✅ Pull requests to `main`
- ✅ Manual trigger (workflow_dispatch)

---

## 📱 Quick Start Commands

```bash
# Make a change
echo "// Test" >> app/build.gradle.kts

# Commit and push
git add .
git commit -m "Trigger build"
git push origin main

# Watch build progress
# Open: https://github.com/lobbbin/cssim/actions
```

---

## 🔗 Useful Links

| Resource | URL |
|----------|-----|
| **Repository** | https://github.com/lobbbin/cssim |
| **Actions Tab** | https://github.com/lobbbin/cssim/actions |
| **Secrets Settings** | https://github.com/lobbbin/cssim/settings/secrets/actions |
| **Gmail App Passwords** | https://myaccount.google.com/apppasswords |
| **Workflow File** | `.github/workflows/android-build.yml` |

---

## ✅ Checklist

Before your first build, verify:

- [ ] EMAIL_USERNAME secret added
- [ ] EMAIL_PASSWORD secret added (App Password)
- [ ] 2-Step Verification enabled on Gmail
- [ ] Workflow is enabled in Actions tab
- [ ] You have push access to the repository

---

## 🎉 You're Ready!

Push any change to trigger your first automated build:

```bash
git push origin main
```

Then watch the magic happen at:
**https://github.com/lobbbin/cssim/actions**

**Expected:**
- ✅ Build completes in ~3 minutes
- 📧 Email arrives at lobinpek2@gmail.com
- 📦 APK available for download

---

<div align="center">

**Built with ❤️ for Country Simulator (cssim)**

[⬆ Back to top](#-github-actions-build-setup-guide)

</div>
