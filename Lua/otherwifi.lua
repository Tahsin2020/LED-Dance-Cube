
wifi.setmode(wifi.STATIONAP)
wifi.ap.config({ssid="NewWifi", auth=wifi.OPEN})
-- connect to WiFi access point (DO NOT save config to flash)
--station_cfg={}
--station_cfg.ssid = "SM-G973W5396"
--station_cfg.pwd = "6049961862"
--station_cfg.ssid = "AndroidAP"
--station_cfg.pwd = "Benny123"
--station_cfg.ssid = "Tahsin's iPhone"
--station_cfg.pwd = "cBT1-O9Tj-HsFq-EL20"
--station_cfg.pwd = "aaaaaaaa"
--station_cfg.save = true
--wifi.setmode(wifi.STATION)
----
--wifi.sta.config(station_cfg)
--wifi.sta.connect()


--local adder = function()
--     ip, nm, gw = wifi.sta.getip()
--     if ip then
--        cfg =
--        {
--            ip="192.168.209.228",
--            netmask="255.255.255.0",
--            gateway="192.168.209.164"
--        }
--        wifi.sta.setip(cfg)
----     print("\nIP Info:\nIP Address: "..ip.." \nNetmask: "..nm.." \nGateway Addr: "..gw.."\n")
----     else
----     print("fail")
----     end
--end
----
-- Check the connection status every 1 second
--mytimer = tmr.create()
--mytimer:register(10000, tmr.ALARM_AUTO, adder)
--mytimer:interval(5000) -- actually, 3 seconds is better!
--mytimer:start()

-- uart.write(0, "Tahsin UART tester \n")

-- server listens on 80, if data received, print data to console and send "hello world" back to caller
-- 30s time out for a inactive client
sv = net.createServer(net.TCP, 30)

recieved_data = "hi"

function receiver(sck, data)
  -- print("hi")
  -- recieved_data = (data)
  uart.write(0,data)
--  str:gsub(".", function(c)
--    value = string.byte(c)
--    -- do something with c
--    end)
--  sck:close()
end

--if sv then
--  sv:listen(12345, function(conn)
--    conn:on("receive", receiver)
--    conn:send("hello world")
--  end)
--end

if sv then
  sv:listen(80, function(conn)
    conn:on("receive", receiver)
    conn:send("hello world")
  end)
end

cfg =
{
    ip="192.168.4.1",
    netmask="255.255.255.0",
    gateway="192.168.4.1"
}
wifi.ap.setip(cfg)

-- ip, nm, gw = wifi.sta.getip()
-- if ip then
-- print("\nIP Info:\nIP Address: "..ip.." \nNetmask: "..nm.." \nGateway Addr: "..gw.."\n")
-- else
-- print("fail")
-- end
