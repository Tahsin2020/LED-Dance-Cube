import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.animation import FuncAnimation, PillowWriter
from create_moving_8x8 import create_animation
from rotating_wall_frames import create_rotating_wall_animation
from wave_animation import create_wave_animation
from cone_animation import create_cone_animation
from hourglass_animation import create_hourglass_animation
from rolling_ball_animation import create_rolling_ball_animation

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

# Replace this with your input_frames array
# input_frames_list = create_animation(input_plane)
# input_frames = np.array(input_frames_list)

input_frames_list = create_rotating_wall_animation()
# input_frames = np.array(input_frames_list)

# input_frames_list = create_wave_animation()
# input_frames_list = create_cone_animation()
# input_frames_list = create_rolling_ball_animation()
input_frames = np.array(input_frames_list)


def get_frame_data(frame):
    data = input_frames[frame]
    x, y, z = np.where(data == 1)
    return x, y, z

def init():
    scatters = ax.scatter([], [], [], c=[])
    return scatters,

def update_graph(frame):
    ax.clear()

    ax.patch.set_facecolor('black')
    ax.grid(False)

    x, y, z = get_frame_data(frame)
    scatters = ax.scatter(x, y, z, c='blue', s=40, edgecolors='white')

    ax.set_xlim(0, 7)
    ax.set_ylim(0, 7)
    ax.set_zlim(0, 7)

    ax.set_xticklabels([])
    ax.set_yticklabels([])
    ax.set_zticklabels([])
    ax.set_xticks([])
    ax.set_yticks([])
    ax.set_zticks([])

    rotation_step = 2
    ax.view_init(30, frame * rotation_step)

    return scatters,

num_frames = input_frames.shape[0]

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
fig.patch.set_facecolor('black')

animation = FuncAnimation(fig, update_graph, init_func=init, frames=num_frames, interval=5, blit=False)

animation.save('l2c-l2c-41/Cube_Driver_Hardware/LED_cube_controller/Animations/Python Animations/rotating_3d_plots.gif', writer=PillowWriter(fps=20), dpi=80)

plt.show()