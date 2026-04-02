package dev.dubhe.april.client.feat.camera;

import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class FreeCam {

    public static final FreeCam INSTANCE = new FreeCam();

    private final Minecraft mc = Minecraft.getInstance();
    private final Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
    private final Vector3f forwards = new Vector3f(0.0F, 0.0F, 1.0F);
    private final Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F);
    private final Vector3f left = new Vector3f(1.0F, 0.0F, 0.0F);
    private boolean active;
    private @Nullable CameraType oldCameraType;
    private @Nullable ClientInput playerInput;
    private double x, y, z;
    private float yRot, xRot;

    private FreeCam() {

    }

    public boolean isActive() {
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public void toggle() {
        if (active) {
            disable();
        } else {
            enable();
        }
    }

    public void enable() {
        if (active) {
            return;
        }

        Entity entity = mc.getCameraEntity();
        if (mc.player == null || entity == null) {
            return;
        }

        active = true;
        playerInput = mc.player.input;
        mc.player.input = createFreeCamInput();
        switchCameraType(CameraType.THIRD_PERSON_BACK);

        float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        Vec3 pos = entity.getEyePosition(partialTicks);
        x = pos.x;
        y = pos.y;
        z = pos.z;
        yRot = entity.getViewYRot(partialTicks);
        xRot = entity.getViewXRot(partialTicks);

        calculateVectors();

        double distance = -2;
        Vec3 horizontalForward = getHorizontalForward();
        x += horizontalForward.x * distance;
        z += horizontalForward.z * distance;
    }

    public void disable() {
        if (!active) {
            return;
        }

        assert mc.player != null;

        active = false;
        if (oldCameraType != null) {
            mc.options.setCameraType(oldCameraType);
        }
        if (playerInput != null) {
            mc.player.input = playerInput;
        }
        if (oldCameraType != null) {
            switchCameraType(oldCameraType);
        }
    }

    private ClientInput createFreeCamInput() {
        return new ClientInput();
    }

    private void calculateVectors() {
        rotation.rotationYXZ(-yRot * ((float) Math.PI / 180F), xRot * ((float) Math.PI / 180F), 0.0F);
        forwards.set(0.0F, 0.0F, 1.0F).rotate(rotation);
        up.set(0.0F, 1.0F, 0.0F).rotate(rotation);
        left.set(1.0F, 0.0F, 0.0F).rotate(rotation);
    }

    private void switchCameraType(CameraType type) {
        oldCameraType = mc.options.getCameraType();
        mc.options.setCameraType(type);
        if (oldCameraType.isFirstPerson() != mc.options.getCameraType().isFirstPerson()) {
            mc.gameRenderer.checkEntityPostEffect(mc.options.getCameraType().isFirstPerson() ? mc.getCameraEntity() : null);
        }
        mc.levelRenderer.needsUpdate();
    }

    public boolean onPlayerTurn(double xRot, double yRot) {
        if (active) {
            this.xRot += (float) xRot * 0.15F;
            this.yRot += (float) yRot * 0.15F;
            this.xRot = Mth.clamp(this.xRot, -90, 90);
            calculateVectors();
            return false;
        } else {
            return true;
        }
    }

    public void onLevelChange() {
        disable();
    }

    long lastTime;
    private double forwardVelocity;
    private double leftVelocity;

    public void onRenderTickStart() {
        if (!active) {
            return;
        }


        if (lastTime == 0) {
            lastTime = System.nanoTime();
            return;
        }

        long currTime = System.nanoTime();
        float frameTime = (currTime - lastTime) / 1e9f;
        lastTime = currTime;

        ClientInput input = playerInput;
        if (input == null) return;
        float forwardImpulse = (input.keyPresses.forward() ? 1 : 0) + (input.keyPresses.backward() ? -1 : 0);
        float leftImpulse = (input.keyPresses.left() ? 1 : 0) + (input.keyPresses.right() ? -1 : 0);
        float upImpulse = (input.keyPresses.jump() ? 1 : 0) + (input.keyPresses.shift() ? -1 : 0);
        double slowdown = Math.pow(0.01, frameTime);
        forwardVelocity = combineMovement(forwardVelocity, forwardImpulse, frameTime, slowdown);
        leftVelocity = combineMovement(leftVelocity, leftImpulse, frameTime, slowdown);
        Vec3 horizontalForward = getHorizontalForward();
        Vec3 horizontalLeft = getHorizontalLeft(horizontalForward);

        double dx = horizontalForward.x * forwardVelocity + horizontalLeft.x * leftVelocity;
        double dy = upImpulse * 50.0;
        double dz = horizontalForward.z * forwardVelocity + horizontalLeft.z * leftVelocity;
        dx *= frameTime;
        dy *= frameTime;
        dz *= frameTime;
        double speed = new Vec3(dx, dy, dz).length() / frameTime;
        if (speed > 50) {
            double factor = 50 / speed;
            forwardVelocity *= factor;
            leftVelocity *= factor;
            dx *= factor;
            dy *= factor;
            dz *= factor;
        }
        x += dx;
        y += dy;
        z += dz;
    }

    private Vec3 getHorizontalForward() {
        double forwardX = this.forwards.x();
        double forwardZ = this.forwards.z();
        double horizontalLengthSquared = forwardX * forwardX + forwardZ * forwardZ;

        if (horizontalLengthSquared < 1.0E-6) {
            // Keep forward movement stable when the camera looks almost straight up/down.
            double yawRad = -Math.toRadians(this.yRot);
            return new Vec3(Math.sin(yawRad), 0.0, Math.cos(yawRad));
        }

        double inverseLength = 1.0 / Math.sqrt(horizontalLengthSquared);
        return new Vec3(forwardX * inverseLength, 0.0, forwardZ * inverseLength);
    }

    private Vec3 getHorizontalLeft(Vec3 horizontalForward) {
        return new Vec3(horizontalForward.z, 0.0, -horizontalForward.x);
    }

    public void onClientTickStart() {
        if (active) {
            disableKey(mc.options.keyTogglePerspective);

            if (mc.player != null && mc.player.input != playerInput && playerInput != null) {
                // don't tick here, since vanilla code will run tick during LocalPlayer.aiStep
                playerInput.tick();
            }
        }
    }

    private void disableKey(KeyMapping key) {
        //noinspection StatementWithEmptyBody
        while (key.consumeClick()) ;
        key.setDown(false);
    }

    private double combineMovement(double velocity, double impulse, double frameTime, double slowdown) {
        if (impulse != 0) {
            if (impulse > 0 && velocity < 0) {
                velocity = 0;
            }
            if (impulse < 0 && velocity > 0) {
                velocity = 0;
            }
            velocity += (double) 50 * impulse * frameTime;
        } else {
            velocity *= slowdown;
        }
        return velocity;
    }
}