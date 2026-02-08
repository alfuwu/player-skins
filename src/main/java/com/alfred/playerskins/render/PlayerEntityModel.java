package com.alfred.playerskins.render;

import com.alfred.playerskins.config.Config;
import com.alfred.playerskins.interfaces.PlayerQuaddablePart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class PlayerEntityModel extends BipedEntityModel {
    public ModelPart leftSleeve;
    public ModelPart rightSleeve;
    public ModelPart leftPants;
    public ModelPart rightPants;
    public ModelPart jacket;
    public boolean classic;

    public PlayerEntityModel(boolean slim, boolean classic) {
        super();
        this.classic = classic;

        if (!classic) {
            if (slim) {
                leftArm = create(32, 48, -1, -2, -2, 3, 12, 4, 0, 5.0f, 2.5f, 0.0f);
                rightArm = create(40, 16, -2, -2, -2, 3, 12, 4, 0, -5.0f, 2.5f, 0.0f);
            } else {
                ((PlayerQuaddablePart)leftArm).setUV(32, 48); // normally, this wouldn't work, but we're calling .convertToPlayerQuads later, which recomputes the model's quads (and thus the UVs too)
            }
            ((PlayerQuaddablePart)leftLeg).setUV(16, 48);
        }

        int armSize = slim && !classic ? 3 : 4;
        leftSleeve = create(48, 48, -1, -2, -2, armSize, 12, 4, 0.25f, 5.0f, 2.5f, 0.0f);
        rightSleeve = create(40, 32, -2, -2, -2, armSize, 12, 4, 0.25f, -5.0f, 2.5f, 0.0f);
        leftPants = create(0, 48, -2, 0, -2, 4, 12, 4, 0.25f, 1.9f, 12.0f, 0.0f);
        rightPants = create(0, 32, -2, 0, -2, 4, 12, 4, 0.25f, -1.9f, 12.0f, 0.0f);
        jacket = create(16, 32, -4, 0, -2, 8, 12, 4, 0.25f);

        ((PlayerQuaddablePart)leftArm).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)head).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)hat).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)body).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)rightArm).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)leftArm).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)rightLeg).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)leftLeg).convertToPlayerQuads(classic);
        ((PlayerQuaddablePart)ears).convertToPlayerQuads(classic);
    }

    @Override
    public void render(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
        super.render(limbAngle, limbDistance, animationProgress, headYaw, headPitch, scale);
        if (classic || !Config.allowMultipleLayers)
            return;

        leftSleeve.render(scale);
        rightSleeve.render(scale);
        leftPants.render(scale);
        rightPants.render(scale);
        jacket.render(scale);
    }

    @Override
    public void setAngles(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
        super.setAngles(limbAngle, limbDistance, animationProgress, headYaw, headPitch, scale);
        copyTrans(leftArm, leftSleeve);
        copyTrans(rightArm, rightSleeve);
        copyTrans(leftLeg, leftPants);
        copyTrans(rightLeg, rightPants);
        copyTrans(body, jacket);
        copyTrans(head, hat); // fixes crouching bug
    }

    private static void copyTrans(ModelPart theFirst, ModelPart the2nd) {
        the2nd.yaw = theFirst.yaw;
        the2nd.pitch = theFirst.pitch;
        the2nd.roll = theFirst.roll;
        the2nd.pivotX = theFirst.pivotX;
        the2nd.pivotY = theFirst.pivotY;
        the2nd.pivotZ = theFirst.pivotZ;
    }

    private ModelPart create(int u, int v, float oX, float oY, float oZ, int sX, int sY, int sZ, float dilation) {
        ModelPart part = new ModelPart(u, v);
        part.addCuboid(oX, oY, oZ, sX, sY, sZ, dilation);
        return ((PlayerQuaddablePart)part).convertToPlayerQuads(classic);
    }

    private ModelPart create(int u, int v, float oX, float oY, float oZ, int sX, int sY, int sZ, float dilation, float pX, float pY, float pZ) {
        ModelPart part = new ModelPart(u, v);
        part.addCuboid(oX, oY, oZ, sX, sY, sZ, dilation);
        part.setPivot(pX, pY, pZ);
        return ((PlayerQuaddablePart)part).convertToPlayerQuads(classic);
    }
}
