# Things I would have liked to get to but wasn't able to

* Unit Tests for DateFileReader
    * file does not exist
    * bad date in file
    * multiline nominal case
* Unit Tests for RoverApiWrapper
    * Add another Constructor to pass in a mocked Client and verify expected target and queryParam methods are called
* Fix type cast assumption of HttpURLConnection in ImageDownlaoder
* Fix assumption that all images are jpgs in ImageDonwloader
* Improve argument parsing for allowing more user control
    * Pass in api_key
    * Pass in mode (allDownload or randomDownload)
    * Pass in rover desired
    * Pass in image download directory
* Input protection
    * Date valid for specified rover (Rover was on mars and operational)
    * Rover exists (once it can be specified by user)
    * Camera exists for rover (once specifiation of camera added)
* Cache management for downloaded files
* Talk to Customer about modifying api to allow specification of camera desired
* Docker Image
* Kubernetes Helm chart
* Switch to service
* Add UI


