# GitHub Actions Secrets Configuration
# =====================================
# 
# To enable email notifications, you need to add these secrets in GitHub:
# 
# 1. Go to: https://github.com/lobbbin/cssim/settings/secrets/actions
# 2. Click "New repository secret"
# 3. Add the following secrets:

# EMAIL_USERNAME
# --------------
# Your Gmail address (or SMTP server username)
# Example: your-email@gmail.com
# Name: EMAIL_USERNAME
# Value: lobinpek2@gmail.com

# EMAIL_PASSWORD
# --------------
# Your Gmail App Password (NOT your regular password)
# 
# To create an App Password:
# 1. Go to: https://myaccount.google.com/security
# 2. Enable 2-Step Verification (if not already enabled)
# 3. Go to: https://myaccount.google.com/apppasswords
# 4. Select "Mail" and "Other (Custom name)"
# 5. Enter "GitHub Actions" as the name
# 6. Click "Generate"
# 7. Copy the 16-character password (no spaces)
# 
# Name: EMAIL_PASSWORD
# Value: [your-16-character-app-password]

# =====================================
# Required Secrets Summary:
# =====================================
# 
# | Secret Name      | Description                    | Example                    |
# |------------------|--------------------------------|----------------------------|
# | EMAIL_USERNAME   | Your Gmail address             | lobinpek2@gmail.com        |
# | EMAIL_PASSWORD   | Gmail App Password (16 chars)  | abcd efgh ijkl mnop        |
# 
# =====================================
