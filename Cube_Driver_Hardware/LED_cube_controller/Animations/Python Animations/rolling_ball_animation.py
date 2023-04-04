import numpy as np

def hill_plane(x):
    return -((1 / 4) * x - np.sqrt(6)) ** 2 + 6

def generate_ball_frame(frame_number, num_frames):
    frame = np.zeros((8, 8, 8))

    # Draw the hill plane and fill all LEDs under the hill
    for x in range(8):
        for y in range(8):
            z = int(hill_plane(x))
            if 0 <= z < 8:
                frame[x, :y+1, 0:z] = 1

    # Calculate the ball position with acceleration
    ball_x = 6 - (int(6 * (frame_number / ((num_frames / 3) - 1)) ** 2)) % 6

    # Add the ball to the frame (5 points in 3D)
    if 0 <= ball_x < 7:
        ball_y = int(hill_plane(ball_x))
        ball_positions = [(ball_x, ball_y, ball_x),
                          (ball_x - 1, ball_y, ball_x),
                          (ball_x, ball_y - 1, ball_x),
                          (ball_x + 1, ball_y, ball_x),
                          (ball_x, ball_y + 1, ball_x),
                          (ball_x, ball_y, ball_x - 1),
                          (ball_x, ball_y, ball_x + 1)]
        for pos in ball_positions:
            x, y, z = pos
            if 0 <= x < 8 and 0 <= y < 8 and 0 <= z < 8:
                frame[pos] = 1

    return frame

num_frames = 150
ball_frames = [generate_ball_frame(i, num_frames) for i in range(num_frames)]

def create_rolling_ball_animation():
    num_frames = 150
    ball_frames = [generate_ball_frame(i, num_frames) for i in range(num_frames)]
    return ball_frames