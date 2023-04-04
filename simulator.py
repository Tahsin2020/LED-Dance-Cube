import matplotlib.pyplot as plt
import numpy as np
import socket
import time
from matplotlib.animation import FuncAnimation

#Method to convert bytes
def bytes_to_coords(bytes_string):
    print(bytes_string)
    binary_strings = [format(byte, '08b') for byte in bytes_string]
    bits = [int(bit) for bit in ''.join(binary_strings)]
    l = [[[0] * 8 for _ in range(8)] for _ in range(8)]
    for i, bit in enumerate(bits):
        y, f = divmod(i, 64)
        x, z = divmod(f, 8)
        l[y][x][z] = bit

    xs = []
    ys = []
    zs = []
    for y in range(len(l)):
        for x in range(len(l[0])):
            for z in range(len(l[0][0])):
                if l[y][x][z] == 1:
                    xs.append(x)
                    ys.append(y)
                    zs.append(z)
                

    return (xs, ys, zs)

# Define the host and port to listen on
# HOST = '128.189.240.39'
HOST = '128.189.246.47'
PORT = 12345



# Define the function to accept connections from clients and read data
def accept_connections():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  # Allow reuse of address
        try:
            s.bind((HOST, PORT))
            s.listen()
            print(f'Server listening on {HOST}:{PORT}...')
        except socket.error as e:
            print(f"Error binding to {HOST}:{PORT}: {e}")
            return

        try:
            conn, addr = s.accept()
        except KeyboardInterrupt:
            print('Server shutting down...')
            exit(0)
        except Exception as e:
            print(f'Error accepting connection: {e}')

        print(f'Connected by {addr}')
        while True:
            try:
                data = conn.recv(64)
            except socket.error as e:
                print(f"Error receiving data: {e}")
                break
            if not data:
                print("NO DATA RECEIVED")
                break
            try:
                print("RECEIVED DATA")
                xs, ys, zs = bytes_to_coords(data)
                print("CONVERTED DATA")
                yield (xs, ys, zs)
            except (ValueError, UnicodeDecodeError) as e:
                print(f"Error decoding data: {e}")
                break

# Define the function to update the plot
def update(frame):
    try:
        xs, ys, zs = next(connection)
    except StopIteration as e:
        print("Iteration stopped")
        exit(0)
        
    # Clear the plot and create a new scatter plot with the incoming data
    ax.clear()
    print("xs: {}".format(xs))
    print("ys: {}".format(ys))
    print("zs: {}".format(zs))
    ax.scatter(xs, zs, ys, marker='o')
    # Set labels and title
    ax.set_xlabel('X Label')
    ax.set_ylabel('Z Label')
    ax.set_zlabel('Y Label')
    ax.set_title('3D Scatter Plot')
    ax.set_xlim([0,7])
    ax.set_ylim([0,7])
    ax.set_zlim([0,7])
    # Pause for a short time to allow the plot to update
    plt.pause(0.001)

fig = plt.figure()
ax = fig.add_subplot(projection='3d')
connection = accept_connections()

# Set up the animation
ani = FuncAnimation(fig, update, interval=10)
# Show the plot
plt.show()
