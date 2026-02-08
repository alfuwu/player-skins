package com.alfred.playerskins.mixin.client;

import com.alfred.playerskins.interfaces.PlayerQuaddablePart;
import com.alfred.playerskins.render.PlayerQuad;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.Quad;
import net.minecraft.client.model.Vertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements PlayerQuaddablePart {
    @Shadow private Vertex[] corners;
    @Shadow private Quad[] faces;
    @Shadow public boolean mirror;
    @Shadow public int u;
    @Shadow public int v;

    @Unique private int sizeX;
    @Unique private int sizeY;
    @Unique private int sizeZ;

    @Inject(method = "addCuboid(FFFIIIF)V", at = @At("RETURN"))
    private void storeSize(float x, float y, float z, int sizeX, int sizeY, int sizeZ, float dilation, CallbackInfo ci) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    public ModelPart convertToPlayerQuads(boolean isClassic) {
        Vertex var26 = corners[0];
        Vertex var12 = corners[1];
        Vertex var13 = corners[2];
        Vertex var14 = corners[3];
        Vertex var15 = corners[4];
        Vertex var16 = corners[5];
        Vertex var17 = corners[6];
        Vertex var18 = corners[7];

        // kill me
        this.faces[0] = new PlayerQuad(new Vertex[]{var16, var12, var13, var17}, u + sizeZ + sizeX, v + sizeZ, u + sizeZ + sizeX + sizeZ, v + sizeZ + sizeY, isClassic);
        this.faces[1] = new PlayerQuad(new Vertex[]{var26, var15, var18, var14}, u, v + sizeZ, u + sizeZ, v + sizeZ + sizeY, isClassic);
        this.faces[2] = new PlayerQuad(new Vertex[]{var16, var15, var26, var12}, u + sizeZ, v, u + sizeZ + sizeX, v + sizeZ, isClassic);
        this.faces[3] = new PlayerQuad(new Vertex[]{var13, var14, var18, var17}, u + sizeZ + sizeX, v, u + sizeZ + sizeX + sizeX, v + sizeZ, isClassic);
        this.faces[4] = new PlayerQuad(new Vertex[]{var12, var26, var14, var13}, u + sizeZ, v + sizeZ, u + sizeZ + sizeX, v + sizeZ + sizeY, isClassic);
        this.faces[5] = new PlayerQuad(new Vertex[]{var15, var16, var17, var18}, u + sizeZ + sizeX + sizeZ, v + sizeZ, u + sizeZ + sizeX + sizeZ + sizeX, v + sizeZ + sizeY, isClassic);
        if (this.mirror)
            for (Quad face : this.faces)
                face.flip();
        return (ModelPart)(Object)this;
    }

    @Override
    public void setUV(int u, int v) {
        this.u = u;
        this.v = v;
    }
}
