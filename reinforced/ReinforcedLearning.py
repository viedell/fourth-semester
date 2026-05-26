import gymnasium as gym
import numpy as np
import random

env = gym.make("FrozenLake-v1", is_slippery=False, render_mode="human")
q_table = np.zeros((16, 4)) 
FORWARD_ACTIONS = [1, 2] 

successes = 0
failures = 0

try:
    for episode in range(500):
        state, _ = env.reset()
        terminated = truncated = False
        
        while not (terminated or truncated):
            if random.uniform(0, 1) < 0.5: 
                action = random.choice(FORWARD_ACTIONS)
            else:
                action = FORWARD_ACTIONS[np.argmax([q_table[state, a] for a in FORWARD_ACTIONS])]
                
            new_state, reward, terminated, truncated, _ = env.step(action)
            
            future_q = np.max([q_table[new_state, a] for a in FORWARD_ACTIONS])
            q_table[state, action] += 0.8 * (reward + 0.95 * future_q - q_table[state, action])
            
            state = new_state
            
            if terminated:
                if reward == 1.0:
                    successes += 1
                else:
                    failures += 1
                    
        print(f"Episode: {episode + 1}/500 | Successes: {successes} | Failures: {failures}", end="\r")

except KeyboardInterrupt:
    pass
finally:
    print(f"\nSimulation stopped. Final Score -> Successes: {successes} | Failures: {failures}")
    env.close()