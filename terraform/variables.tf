variable "project_id" {
  description = "ID of the relevant GCP project"
  type        = string
}

variable "region" {
  description = "GCP region to use"
  type        = string
  default     = "us-west1"
}

variable "environment" {
  type        = string
  description = "The environment name the resources are deployed to"
  default     = "dev"
}

variable "team" {
  type        = string
  description = "The owners team name"
  default     = "app"
}
