#!/bin/bash

# Country Simulator - GitHub Repository Setup Script
# This script creates a GitHub repo and pushes all files

set -e  # Exit on error

echo "🎮 Country Simulator - GitHub Repository Setup"
echo "=============================================="
echo ""

# Check if gh is installed
if ! command -v gh &> /dev/null; then
    echo "❌ GitHub CLI (gh) is not installed. Please install it first."
    exit 1
fi

# Check if logged in
if ! gh auth status &> /dev/null; then
    echo "❌ Not logged in to GitHub. Please run: gh auth login"
    exit 1
fi

echo "✅ GitHub CLI is installed and you're logged in"
echo ""

# Get repository name from user
read -p "Enter repository name (default: CountrySimulator): " REPO_NAME
REPO_NAME=${REPO_NAME:-CountrySimulator}

# Get visibility
echo ""
echo "Repository visibility:"
echo "1) Public (anyone can see)"
echo "2) Private (only you can see)"
read -p "Choose (1 or 2, default: 1): " VISIBILITY_CHOICE
VISIBILITY_CHOICE=${VISIBILITY_CHOICE:-1}

if [ "$VISIBILITY_CHOICE" = "2" ]; then
    VISIBILITY="--private"
    VISIBILITY_TEXT="Private"
else
    VISIBILITY="--public"
    VISIBILITY_TEXT="Public"
fi

echo ""
echo "📦 Creating repository: $REPO_NAME ($VISIBILITY_TEXT)"
echo ""

# Create repository
gh repo create "$REPO_NAME" $VISIBILITY --source=. --remote=origin --push

echo ""
echo "✅ Repository created successfully!"
echo ""

# Add all files
echo "📂 Adding all files to git..."
git add .

# Create commit
echo "📝 Creating initial commit..."
git commit -m "Initial commit: Country Simulator - Complete text-based country Android game

🎮 FEATURES:
• 10 core gameplay systems (Elections, Economy, Law, Ministries, Infrastructure, 
  Diplomacy, Demographics, Sports, Procedural Generation, Butterfly Effects)
• 11 game engines (ButterflyEffect, Election, GameState, Campaign, OppositionMode, 
  MacroMicroCascade, InteractiveDecision, DynamicNPC, DynamicEconomy, DynamicWorld, Event)
• 20+ UI screens with Jetpack Compose + Material 3
• Paging 3 for 1000+ item lists without lag
• 10,000+ procedural templates for infinite replayability
• Room Database with 95+ entities, 50+ DAOs
• Complete MVVM architecture

🔧 TECH STACK:
• Kotlin 1.9.20, Java 21
• Jetpack Compose 2023.10.01 (Material 3)
• Room 2.6.1 (Database)
• Paging 3 3.2.1 (Lazy loading)
• Coroutines/Flow (Async)
• WorkManager (Background tasks)

📊 STATS:
• ~40,000 lines of code
• 80+ Kotlin files
• 95+ data entities
• 50+ DAOs
• 11 game engines
• 20+ UI screens
• 10,000+ procedural templates

🎯 GAMEPLAY:
• 100+ interactive micro-tasks
• 10 macro decision types
• 500+ dynamic NPCs with AI personalities
• 10 economic sectors with supply/demand
• 15+ AI countries with goals
• Dynamic alliances that form/collapse
• Wars that spread regionally
• Every decision has consequences

Built for Android 7.0+ (API 24+)"

# Push to main branch
echo "🚀 Pushing to GitHub..."
git branch -M main
git push -u origin main

echo ""
echo "=============================================="
echo "✅ SUCCESS! Repository created and pushed!"
echo "=============================================="
echo ""
echo "📱 Your repository is now live at:"
echo "   https://github.com/$(gh api user | jq -r .login)/$REPO_NAME"
echo ""
echo "🎮 Next steps:"
echo "   1. Open the repository on GitHub"
echo "   2. Add screenshots (optional)"
echo "   3. Share with the world!"
echo ""
echo "📱 To build and run:"
echo "   1. Open in Android Studio"
echo "   2. Sync Gradle"
echo "   3. Build → Make Project"
echo "   4. Run on Android 7.0+ device"
echo ""
