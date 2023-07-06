# ProjektGolfApp2

This app is an improvment on the app held in the ProjectGolfApp1 repository.

Here I will describe how is it used.

Login screen:
Here every user can login with username and password.

![Login screen](/screenshots/PG2_login.jpg)

There is a separate register screen (username, password, password again)

![Register screen](/screenshots/PG2_register.jpg)

Main screen:
Once the user is logged in, by defualt he can't see any cars on map. He has to go to the settings page to setup devices.
If the user has done that he can see the path of a car.

The page is automaticly refreshed every 10 seconds if the button in the middle is green.
Every time that a user interacts with the map by zooming in/out or moving the map the button turns red and auto-refresh stops and is activated again by clicking the middle button.

![Main screen](/screenshots/PG2_main.jpg)


Settings screen:

There are several things that a user can do in the settings screen:

    - logout
    
    - delete user (separate screen)
   
    - change password (separate screen)
    
    - select time interval for car path for the main screen:
    
        - certain time from current moment (10 minutes to 180 days) (screenshot at the end)
        
        - select timestamp of start and end  (screenshot at the end)
   
    - select what cars location to you want to see in the main screen (can have unlimited cars, altough only 2 cars atm have that kind of a device)
    
    - navigate to add new device screen    

![Settings screen](/screenshots/PG2_settings.jpg)
![Settings screen - logout](/screenshots/PG2_logout.jpg)


Delete user screen:
User needs to enter his password to delete the user account.

![Delete user screen](/screenshots/PG2_deleteUser.jpg)


Change password screen:

![Change password screen](/screenshots/PG2_chPASS.jpg)


Add device screen:

To add a device you need devices credentials. Until you add a device you can't see anything on the main screen.

To obtain the credentials, it is necessary to contact Kristian directly.
The information linked to these credentials includes the location data of Kristian's car as well as the historical records of the car's whereabouts.
If you are considering employing Kristian, you can certainly obtain the credentials by reaching out to him personally.

![Add device screen](/screenshots/PG2_addDevice.jpg)

Bluetooth screen:

Pressings the buttons the user interacts via bluetooth with the device.
The user chooses a command which does one of few things:

    - device stops sending data to the server for a certain number of minutes (T=222,??? - ??? - number of minutes)
    - starts charging the internal battery of the device (using the main car battery)
    - resets the device

![Bluetooth screen](/screenshots/PG2_bluetooth.jpg)
![Bluetooth screen](/screenshots/PG2_bluetooth_selectCommand.jpg)










Additional settings screenshots:
![Settings screen - calendar](/screenshots/PG2_settings_calendar.jpg)
![Settings screen - time](/screenshots/PG2_settings_time.jpg)
![Settings screen - history interval](/screenshots/PG2_bluetooth_selectCommand.jpg)






