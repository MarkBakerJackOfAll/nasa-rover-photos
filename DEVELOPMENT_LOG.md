## Aproach

The breif given describing the problems was very breif (I like puns), and left me with quite a few questions.  So my plan is to document the relevant questions I had along the way and document the assumptions I made to answer them.  In an ideal world, many of these questions would be directed at the Product Owner or customer.  That would give you a product that is more likely to meet all the customer's desires, but the trade off is that it takes more time.  So for the sake of time, I will do my best to select reasonable answers.

Also to ensure I don't just duplicate what is in your example, I am attempting to keep looking at it to a minimum, and will not be making any decisions based off of what is in that repo.

### First Look at Requirements

#### Core Requirements

* Check in code in GitHub
    * Send Link to matt.hawkes@livingasone.com
* Use NASA mars rover API
    * Will likely mean downloading a client dependency from them
        * Looks like I was wrong and they don't release a client package.  Standard HTTPS it is
* Selects a picture on a given day
    * Input = day, Output = picture
    * Day format examples that need to be supported
        * 02/27/17
        * June 2, 2018
        * Jul-13-2016
        * April 31, 2018
    * Dates must be read in from a text file
* Download and store image locally
* Code must be in Java
* Need to be able to run and build locally
    * Need to Include script for building (gradle since I am familiar and example uses it)
    * Need to Include instructions for building
    * Need to Include instructions for running
* Documentation
    * Development Process
    * Build/Run Instructions

#### Bonuses

* Unit Tests
    * Definitely
* Static Analysis
    * Time dependent
* Performance tests
    * Time dependent
* Display image in web browser
    * Would look good, more work, do other things first
* Run in Docker
    * More environment setup required on my local dev box, do last

### First Questions

* Dates in Text file
    * Are the listed date formats the only ones required?
        * Assumption: Yes, if other formats are easily supportable great.  Ensure good user feedback for unsupported formats
    * Where does the text file need to be stored?
        * Assumption: Defautl to /tmp/inputFile.txt, but can be overridden
    * Are dates newline separated?
        * Assumption: Yes
    * Input protection is a concern here?
        * Assumption: Yes, invalid data formats, and files that would result in too many requests for the filesystem to handler or for the API key used
* Mars Rover API
    * There are multiple mars rovers, each with multiple cameras, each of which can take multiple pictures in a day.  Was the desire really only for one of those pictures?
        * Assumption: Yes only one picture at a time is desired, with the ability to grab multiple pictures.  Start easy with hardcoded rover name, ignore camera name and select the a random picture returned by the query.  Then add ways of providing input to select which rover and which camera, then the ability to select which picture.
    * Api key.  There is a demo api key that limits queries to 50 per hour.  That definitly isn't enough for my development, but is it enough for the user? Should I check in my development key?
        * Assumption: Definitely not check in my development key as that would be a security issue.  Start with demo api key and possibly add instructions for providing own.  Bonus points if you can automate getting a new api key from NASA for the user (Might not be possible).
* Image storage
    * Where on disk do you want the images?
        * Assumption: /tmp/imageDownloads
    * Do you want to keep them indefinitely, or do we need to clear them out after some time, or some max number stored?
        * Assumption: When reading in the input file yes they will need to be stored indefinetly so that the customer can verify the files exist. (Might need to input protect the data so not too many can be done at once)  If this were a server the answer is definetely yes.  If we get to displaying the image in a web browser this will probably be wanted.

### Learned along the way

* For the NASA api, the page query parameter is only useful if you want to choose to limit how many responses are returned.  The default is to return all responses.  Unless I hit an issue I will be ignoring this parameter, and use the default to return all pictures.  If we were in a high usage scenario or the pictures being returned were getting used in a different way I might choose differently.
* Tried to use DateFormat.getDateInstance() as a more generic way of parsing the date and it supporting localization of MM/dd/YYYY vs dd/MM/YYYY automatically, but it didn't support YYYY-MM-dd (no format change required) or MMM-d-yyyy (one of the example) formats.
