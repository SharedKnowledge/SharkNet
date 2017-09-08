# SharkNet

## About
[About SharkNet][4]

## Setting Up SharkNet
  - [ ] Checkout the [SharkFW][2] project (NOT SharkNet!)
  - [ ] Install maven integration if you don’t use IntelliJ (which supports maven natively)
  - [ ] After the checkout, run mvn clean install, this will install SharkFW to the local maven repository
  - [ ] Checkout the SharkNet project [Android Studio]
  - [ ] Open a command prompt and navigate to the in step 4 ceated folder of SharkNet
  - [ ] Type and execute the following command: git submodule update --init --recursive –remote
  - [ ] Within the SharKNet project, execute the Gradle command: installDebug
  - [ ] You should now be able to run the project (Run ‘app‘)

## Setting up your mobile phone
  - [ ] It is advisable to test the SharkNet App with one or multiple mobile phones. Emulators are not quite suitable for SharkNet
  - [ ] Go to the run configurations -> General -> switch Target to Open Select Deployment Target Dialog (and later to the option USB Device)
  - [ ] Follow [these instructions][5]

## Inspecting the SQL databases of the app
  - [ ] Install the app on two or more devices
  - [ ] Enable Bluetooth on all devices
  - [ ] Create a new account and search for other chat partners via the „+“ button
  - [ ] Send multiple messages to the chat partner
  - [ ] Check if the other partner received the messages
  - [ ] Connect your mobile phone with a PC
  - [ ] Create a .bat file with the content: „adb pull /sdcard/Android/data/net.sharksystem.sharknet/files“
  - [ ] Move the .bat to a location where the SQLite databases shall be stored
  - [ ] Install SQLite Studio: https://sqlitestudio.pl/index.rvt
  - [ ] Execute the .bat
  - [ ] Open the new ceated files folder, after that open the chats folder
  - [ ] Open the .db file with SQliteStudio, choose the .db file which includes the nicknames of you and your chatpartner, e.g. DustinFMichaelS1503514403815.db
  - [ ] You can now inspect your sent and received messages inside the knowledge table

## Contents
Thee next "graphic" shows the relations between the involved projects. The [SharkNet-Api-Android][1] currently not only uses the Android SharkFW but also inherits it due to some problems reagrding Android-Libraries depending on other Android-Libraries.
**SharkNet**
&nbsp;&nbsp;&nbsp;|
&nbsp;&nbsp;&nbsp;|  *uses*
&nbsp;&nbsp;&nbsp;v
**[SharkNet-Api-Android][1]** => Maps the framework features to objects used in the SharkNet application
&nbsp;&nbsp;&nbsp;|
&nbsp;&nbsp;&nbsp;|  *uses*
&nbsp;&nbsp;&nbsp;v
**Android SharkFW** => Adds Android functionalities like Wifi, Bluetooth, NFC, and GPS
&nbsp;&nbsp;&nbsp;|
&nbsp;&nbsp;&nbsp;|  *extends*
&nbsp;&nbsp;&nbsp;v
**[SharkFW][2]** => The core framework!

## Prerequiries
This project requires the [SharkFW][2] and the [SharkNet-Api-Android][1] to work. The [SharkNet-Api-Android][1] is currently set as a git submodule and also as an Android-Library.

## Installation
Because all projects are developed simultaneously the [SharkFW][2] can not yet be accessed via a external repository. So the only way to us eit is to build it locally. Threfor you need to have **maven3** installed on your machine. After **maven3** is installed and the project was cloned to your local machine you can install it to your *local maven repository*. It is locaated in linux on `~/.m2`. Within the folder of the [SharkFW][2] project you need to execute the following maven command to install this project to your *local maven repository*. `mvn clean install`. This will load all the necessary dependencies and creates a .jar file with the complete framework. Once installed to your *local maven repository* the [SharkNet-Api-Android][1] can access this framework due to the settings made in *gradle*.
**BUT** there is still another thing to do. The [SharkNet-Api-Android][1] is used as an *Android-Library* and also as a *git submodule* so we need to retrieve the latest changes. Once you cloned [this][3] repository you have to inside the folder with `cd SharkNet` and initialize the submodules with the command `git submodule update --init --recursive --remote`. This will initialize all submodules within this project (currently there is jsut one) and load all the data from the remote.

## Future Features
  - [ ] exchange profiles via NFC
  - [ ] Send images, videos and audio in chat

[1]: https://github.com/SharedKnowledge/SharkNet-Api-Android
[2]: https://github.com/SharedKnowledge/SharkFW
[3]: https://github.com/SharedKnowledge/SharkNet
[4]: http://sharedknowledge.github.io/#sharknet
[5]: https://developer.android.com/studio/run/device.html