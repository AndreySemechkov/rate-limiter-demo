terraform {
  backend "gcs" {
    bucket = "homelab-terraform-state-a1b2c3"
    # using OpenTofu vars supported in backend
    prefix = "${var.region}/rate-limiter/terraform.tfstate"
  }
} 