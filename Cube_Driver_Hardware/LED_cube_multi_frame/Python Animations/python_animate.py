import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.animation as animation
from create_moving_8x8 import create_animation

# Example input 8x8 plane
# input_plane = np.array([
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 1, 0, 0, 0, 0, 0, 0],
#     [0, 0, 1, 0, 0, 0, 0, 0],
#     [0, 0, 0, 1, 0, 0, 0, 0],
#     [0, 0, 0, 0, 1, 0, 0, 0],
#     [0, 0, 0, 0, 0, 1, 0, 0],
#     [0, 0, 0, 0, 0, 0, 1, 0],
#     [0, 0, 0, 0, 0, 0, 0, 1]
# ])

input_plane = np.array([
    [1, 0, 0, 1, 0, 1, 1, 1],
    [1, 0, 0, 1, 0, 0, 1, 0],
    [1, 0, 0, 1, 0, 0, 1, 0],
    [1, 1, 1, 1, 0, 0, 1, 0],
    [1, 1, 1, 1, 0, 0, 1, 0],
    [1, 0, 0, 1, 0, 0, 1, 0],
    [1, 0, 0, 1, 0, 0, 1, 0],
    [1, 0, 0, 1, 0, 1, 1, 1]
])

# Generate the animation frames
animation_frames = create_animation(input_plane, move_every_n_frames=5)

print(animation_frames[0:2])

# Create a 3D scatter plot of the LED cube
def plot_cube(cube):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection="3d")
    ax.set_xlim(0, 7)
    ax.set_ylim(0, 7)
    ax.set_zlim(0, 7)

    x, y, z = np.where(cube == 1)
    sc = ax.scatter(x, y, z, c="red", s=100)

    ax.set_xticks(range(8))
    ax.set_yticks(range(8))
    ax.set_zticks(range(8))

    return fig, ax, sc

# Update the scatter plot for each frame
def update(frame, sc, ax):
    x, y, z = np.where(animation_frames[frame] == 1)
    ax.clear()
    ax.set_xlim(0, 7)
    ax.set_ylim(0, 7)
    ax.set_zlim(0, 7)
    ax.set_xticks(range(8))
    ax.set_yticks(range(8))
    ax.set_zticks(range(8))
    sc = ax.scatter(x, y, z, c="blue", s=100)
    return sc,

# Create the animation
fig, ax, sc = plot_cube(animation_frames[0])
ani = animation.FuncAnimation(fig, update, fargs=(sc, ax), frames=150, interval=5000/150, repeat=True)

# Show the animation
plt.show()