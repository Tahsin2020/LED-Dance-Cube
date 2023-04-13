-- Take the user's SSID and password
dofile("credentials.lua")
-- connect to WiFi access point (DO NOT save config to flash)
station_cfg={}
station_cfg.ssid = SSID
station_cfg.pwd = PASSWORD
wifi.setmode(wifi.STATION)
wifi.sta.config(station_cfg)
wifi.sta.connect()

-- Function to loop check if connected to wifi
local check = function()
     ip, nm, gw = wifi.sta.getip()
     if ip then
        -- Do nothing if connected
     else
        station_cfg={}
        station_cfg.ssid = SSID
        station_cfg.pwd = PASSWORD
        wifi.setmode(wifi.STATION)
        wifi.sta.config(station_cfg)
        wifi.sta.connect()
        i=0
     end
end

-- Check the connection status every 5 seconds
mytimer = tmr.create()
mytimer:register(10000, tmr.ALARM_AUTO, check)
mytimer:interval(5000) 
mytimer:start()


sv = net.createServer(net.TCP, 30)

-- Write the UART wired connection
function receiver(sck, data)
  uart.write(0,data)
end

-- Set up a listener at port 12345
if sv then
  sv:listen(12345, function(conn)
    conn:on("receive", receiver)
    conn:send("hello world")
  end)
end
