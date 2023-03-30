import numpy as np
import math

num_frames = 150
num_rotations = 3

def sphere_shape(x, y, z, radius):
    return (x - 3.5)**2 + (y - 3.5)**2 + (z - 3.5)**2 <= radius**2

def generate_sine_wave_sphere_frame(angle, t):
    frame = np.zeros((8, 8, 8))
    for x in range(8):
        for y in range(8):
            for z in range(8):
                x_rot = (x - 3.5) * math.cos(angle) - (y - 3.5) * math.sin(angle) + 3.5
                y_rot = (x - 3.5) * math.sin(angle) + (y - 3.5) * math.cos(angle) + 3.5

                radius = 1.5 + 1.5 * math.sin(2 * math.pi * t + 0.5 * (x_rot + y_rot))
                if sphere_shape(x_rot, y_rot, z, radius):
                    frame[x, y, z] = 1
    return frame

def create_pulsating_wave_sphere_animation():
    frames = []
    for frame in range(num_frames):
        angle = frame / num_frames * num_rotations * 2 * math.pi
        t = frame / num_frames
        frames.append(generate_sine_wave_sphere_frame(angle, t))
    return frames