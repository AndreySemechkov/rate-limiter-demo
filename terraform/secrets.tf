# Generate a strong random password for the secret using the random provider
resource "random_password" "db_password" {
  length           = 32
  special          = true
  upper            = true
  lower            = true
  numeric          = true
  override_special = "!@#%^&*()-_=+[]{}"
}

# Google Secret Manager Secret with automatic replication for high availability and compliance
resource "google_secret_manager_secret" "secret_app" {
  name      = "rate-limiter-db-password"
  secret_id = "rate-limiter-db-password"

  labels = {
    env  = var.environment
    team = var.team
  }

  # Automatic replication is recommended for most use cases
  replication {
    auto {}
  }
}

# Store the generated password as a secret version
resource "google_secret_manager_secret_version" "secret_app_version" {
  secret      = google_secret_manager_secret.secret_app.id
  secret_data = random_password.db_password.result
}