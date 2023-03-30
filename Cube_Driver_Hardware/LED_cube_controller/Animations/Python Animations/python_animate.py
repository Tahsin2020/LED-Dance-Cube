import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.animation as animation
from create_moving_8x8 import create_animation
from matplotlib.animation import FuncAnimation, PillowWriter
from rotating_wall_frames import create_rotating_wall_animation
from wave_animation import create_wave_animation
from cone_animation import create_cone_animation
from rolling_ball_animation import create_rolling_ball_animation
from rotating_diamond_animation import create_rotating_diamond_animation
from pulsating_wave_sphere import create_pulsating_wave_sphere_animation
from helix_animations import create_helix_animation


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
# animation_frames = create_animation(input_plane, move_every_n_frames=5)
# animation_frames = create_rotating_wall_animation()
# animation_frames = create_wave_animation()
# animation_frames = create_rotating_diamond_animation()
# animation_frames = create_pulsating_wave_sphere_animation()
animation_frames = create_helix_animation()
# animation_frames = create_cone_animation()
# animation_frames = create_hourglass_animation()
# animation_frames = create_rolling_ball_animation()

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

    # Set the background color of the 3D plot to black
    ax.patch.set_facecolor('black')
    ax.grid(False)
    sc = ax.scatter(x, y, z, c="blue", edgecolors='black', s=100)
    return sc,

# Create the animation
fig, ax, sc = plot_cube(animation_frames[0])
ani = animation.FuncAnimation(fig, update, fargs=(sc, ax), frames=150, interval=(5000/150)/8, repeat=True, blit=False)

# Save the animation as a GIF
ani.save('HI.gif', writer=PillowWriter(fps=20), dpi=80)

# Show the animation
plt.show()