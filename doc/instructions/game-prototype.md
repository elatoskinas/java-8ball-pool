# Instructions on how to play the Game Prototype
1. Ensure the project is built & synced using Gradle
2. Start DesktopLauncher in desktop/src/com.sem.pool.desktop.DesktopLauncher.java
3. Enter some username & password, and click register
4. Login with the credenetials
5. Now, you can click the left mouse button to fire the cue ball. The direction will be based on cursor position.
6. After all the balls stop moving, a cue shot can be made again by clicking the left mouse button again.

## Notes
* There is quite a lot of internal functionality added in the prototype that handles Game State & ball potting
internally. This is not yet visible in the game itself.
* Upon potting the cue ball, the game cannot proceed without restarting it. This is due to a missing feature that
we have not yet implemented (TBD: might merge this before Sprint deadline)
* Ball collisions can be a bit tricky in the current version. Potting balls might be somewhat hard due to
colliding box size and current  ball collisions, but potting is definitely possible once a ball collides
with a pocket.