wifi.setmode(wifi.STATIONAP)
wifi.ap.config({ssid="DanceCube", auth=wifi.OPEN})

---  Talking to a haywire AI.

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

if sv then
  sv:listen(80, function(conn)
    conn:on("receive", receiver)
    conn:send("hello world")
  end)
end

-- remaining, used, total = file.fsinfo()
-- print("\nFile system info:\nTotal: "..total.." Bytes\nUsed: "..used.." Bytes\nRemaining: "..remaining.." Bytes\n")

cfg =
{
    ip="192.168.4.1",
    netmask="255.255.255.0",
    gateway="192.168.4.1"
}
wifi.ap.setip(cfg)

-- ip, nm, gw=wifi.ap.getip()

-- if ip then
-- print("\nIP Info:\nIP Address: "..ip.." \nNetmask: "..nm.." \nGateway Addr: "..gw.."\n")
-- end
