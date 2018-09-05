# Bose 

The following indicates what's working and what's not working, will update as I make changes:

General Requirements
Yes ->• Must use data received from a real weather service provider (e.g. weather.com API or
other service provider)

Yes ->• Must Display current weather for a location submitted by the user

No ->• Should incorporate a 'weather forecast' UI that is separate from the 'current weather'
UI and is not displayed at the same time

Yes, every 15 minutes, we get a notification ->• Must Update the weather data in the background at least every 1 hours

Yes, every 15 minutes, we get a notification ->• When weather data is fetched a notification Must be displayed to the user that new
weather data is available


Yes/No I set up the Room database, but there's a bug I think so not working right now, will try to fix it tomorrow:
• Must persist the user’s location across app starts/stops

Yes->• Must have a unit test to verify the objects used for fetching weather data

Mile Stone 2:
Yes/No -> only 2 sources actually(Open Weather & Dark Sky)• Must support weather data from at least 3 sources.

Yes -> • Must allow user to select weather source

Yes -> • Should only access one weather source at a time

No->• Should allow a user assign a weather source to preset button in the UI

No->• Must persist last selected weather provider(s) across app starts/stops
