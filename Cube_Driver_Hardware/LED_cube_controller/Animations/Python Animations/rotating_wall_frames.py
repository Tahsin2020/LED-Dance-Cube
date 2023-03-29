import numpy as np

def rotate_point(point, angle):
    x, y, z = point
    s, c = np.sin(np.radians(angle)), np.cos(np.radians(angle))
    x_rot = x * c - y * s
    y_rot = x * s + y * c
    return x_rot, y_rot, z

def generate_rotating_wall_frame(rotation_angle, center_x=3.5, center_y=3.5):
    frame = np.zeros((8, 8, 8))
    wall_points = [(x, y, z) for x in range(8) for y in range(8) for z in range(8) if  x == 4]

    for point in wall_points:
        x, y, z = point
        x_shifted, y_shifted = x - center_x, y - center_y
        rotated_point = rotate_point((x_shifted, y_shifted, z), rotation_angle)
        x_rotated, y_rotated, z_rotated = rotated_point
        x_rotated_shifted, y_rotated_shifted = int(x_rotated + center_x), int(y_rotated + center_y)

        if 0 <= x_rotated_shifted < 8 and 0 <= y_rotated_shifted < 8 and 0 <= z_rotated < 8:
            frame[x_rotated_shifted, y_rotated_shifted, z_rotated] = 1

    return frame

def create_rotating_wall_animation():
    num_frames = 150
    rotation_angle_step = 360 / num_frames
    rotating_wall_frames = [generate_rotating_wall_frame(rotation_angle_step * i) for i in range(num_frames)]
    return rotating_wall_frames