# BrowserAutomate

in scrape.java file change the name string ( which is one u want to search in google )
`scrapeImages(name, 1000);` here u can change number to change the no. of downloads limit
`sleep(100000);` this gives us time to scroll the browser page to load the images. ( u can change the time as u wish )
`ExecutorService executorService = Executors.newFixedThreadPool(20);` here u can change the threadpool number. which will speed up ur download time but it will cost data.

# how to run

once u run the code, it will pop up firefox web browser with the search result ( according to string name )
before the time limit u can scroll the browser page to load the images.
once it finished, it will download the images automatically.
