package ru.megantcs.enhancer.platform.loader.libraries;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsSystem;

@LuaExportClass(name = "RenderSystem")
public class LibraryRenderSystem {

    @LuaExportMethod
    public LibraryRenderSystem() {}

    @LuaExportMethod
    public static void useShader(int code) {
        switch (code) {
            case 0: RenderSystem.setShader(GameRenderer::getPositionProgram); break;
            case 1: RenderSystem.setShader(GameRenderer::getPositionColorProgram); break;
            case 2: RenderSystem.setShader(GameRenderer::getPositionColorTexProgram); break;
            case 3: RenderSystem.setShader(GameRenderer::getPositionTexProgram); break;
            case 4: RenderSystem.setShader(GameRenderer::getPositionTexColorProgram); break;
            case 5: RenderSystem.setShader(GameRenderer::getParticleProgram); break;
            case 6: RenderSystem.setShader(GameRenderer::getPositionColorLightmapProgram); break;
            case 7: RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram); break;
            case 8: RenderSystem.setShader(GameRenderer::getPositionTexColorNormalProgram); break;
            case 9: RenderSystem.setShader(GameRenderer::getPositionTexLightmapColorProgram); break;
            case 10: RenderSystem.setShader(GameRenderer::getRenderTypeSolidProgram); break;
            case 11: RenderSystem.setShader(GameRenderer::getRenderTypeCutoutMippedProgram); break;
            case 12: RenderSystem.setShader(GameRenderer::getRenderTypeCutoutProgram); break;
            case 13: RenderSystem.setShader(GameRenderer::getRenderTypeTranslucentProgram); break;
            case 14: RenderSystem.setShader(GameRenderer::getRenderTypeTranslucentMovingBlockProgram); break;
            case 15: RenderSystem.setShader(GameRenderer::getRenderTypeTranslucentNoCrumblingProgram); break;
            case 16: RenderSystem.setShader(GameRenderer::getRenderTypeArmorCutoutNoCullProgram); break;
            case 17: RenderSystem.setShader(GameRenderer::getRenderTypeEntitySolidProgram); break;
            case 18: RenderSystem.setShader(GameRenderer::getRenderTypeEntityCutoutProgram); break;
            case 19: RenderSystem.setShader(GameRenderer::getRenderTypeEntityCutoutNoNullProgram); break;
            case 20: RenderSystem.setShader(GameRenderer::getRenderTypeEntityCutoutNoNullZOffsetProgram); break;
            case 21: RenderSystem.setShader(GameRenderer::getRenderTypeItemEntityTranslucentCullProgram); break;
            case 22: RenderSystem.setShader(GameRenderer::getRenderTypeEntityTranslucentCullProgram); break;
            case 23: RenderSystem.setShader(GameRenderer::getRenderTypeEntityTranslucentProgram); break;
            case 24: RenderSystem.setShader(GameRenderer::getRenderTypeEntityTranslucentEmissiveProgram); break;
            case 25: RenderSystem.setShader(GameRenderer::getRenderTypeEntitySmoothCutoutProgram); break;
            case 26: RenderSystem.setShader(GameRenderer::getRenderTypeBeaconBeamProgram); break;
            case 27: RenderSystem.setShader(GameRenderer::getRenderTypeEntityDecalProgram); break;
            case 28: RenderSystem.setShader(GameRenderer::getRenderTypeEntityNoOutlineProgram); break;
            case 29: RenderSystem.setShader(GameRenderer::getRenderTypeEntityShadowProgram); break;
            case 30: RenderSystem.setShader(GameRenderer::getRenderTypeEntityAlphaProgram); break;
            case 31: RenderSystem.setShader(GameRenderer::getRenderTypeEyesProgram); break;
            case 32: RenderSystem.setShader(GameRenderer::getRenderTypeEnergySwirlProgram); break;
            case 33: RenderSystem.setShader(GameRenderer::getRenderTypeLeashProgram); break;
            case 34: RenderSystem.setShader(GameRenderer::getRenderTypeWaterMaskProgram); break;
            case 35: RenderSystem.setShader(GameRenderer::getRenderTypeOutlineProgram); break;
            case 36: RenderSystem.setShader(GameRenderer::getRenderTypeArmorGlintProgram); break;
            case 37: RenderSystem.setShader(GameRenderer::getRenderTypeArmorEntityGlintProgram); break;
            case 38: RenderSystem.setShader(GameRenderer::getRenderTypeGlintTranslucentProgram); break;
            case 39: RenderSystem.setShader(GameRenderer::getRenderTypeGlintProgram); break;
            case 40: RenderSystem.setShader(GameRenderer::getRenderTypeGlintDirectProgram); break;
            case 41: RenderSystem.setShader(GameRenderer::getRenderTypeEntityGlintProgram); break;
            case 42: RenderSystem.setShader(GameRenderer::getRenderTypeEntityGlintDirectProgram); break;
            case 43: RenderSystem.setShader(GameRenderer::getRenderTypeTextProgram); break;
            case 44: RenderSystem.setShader(GameRenderer::getRenderTypeTextBackgroundProgram); break;
            case 45: RenderSystem.setShader(GameRenderer::getRenderTypeTextIntensityProgram); break;
            case 46: RenderSystem.setShader(GameRenderer::getRenderTypeTextSeeThroughProgram); break;
            case 47: RenderSystem.setShader(GameRenderer::getRenderTypeTextBackgroundSeeThroughProgram); break;
            case 48: RenderSystem.setShader(GameRenderer::getRenderTypeTextIntensitySeeThroughProgram); break;
            case 49: RenderSystem.setShader(GameRenderer::getRenderTypeLightningProgram); break;
            case 50: RenderSystem.setShader(GameRenderer::getRenderTypeTripwireProgram); break;
            case 51: RenderSystem.setShader(GameRenderer::getRenderTypeEndPortalProgram); break;
            case 52: RenderSystem.setShader(GameRenderer::getRenderTypeEndGatewayProgram); break;
            case 53: RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram); break;
            case 54: RenderSystem.setShader(GameRenderer::getRenderTypeCrumblingProgram); break;
            case 55: RenderSystem.setShader(GameRenderer::getRenderTypeGuiProgram); break;
            case 56: RenderSystem.setShader(GameRenderer::getRenderTypeGuiOverlayProgram); break;
            case 57: RenderSystem.setShader(GameRenderer::getRenderTypeGuiTextHighlightProgram); break;
            case 58: RenderSystem.setShader(GameRenderer::getRenderTypeGuiGhostRecipeOverlayProgram); break;
            default: break;
        }
    }

    @LuaExportMethod
    public static void setShaderColor(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a);
    }

    @LuaExportMethod
    public static float[] getShaderColor() {
        return RenderSystem.getShaderColor();
    }

    @LuaExportMethod
    public static void setShaderGlintAlpha(float alpha) {
        RenderSystem.setShaderGlintAlpha(alpha);
    }

    @LuaExportMethod
    public static float getShaderGlintAlpha() {
        return RenderSystem.getShaderGlintAlpha();
    }

    @LuaExportMethod
    public static void setShaderFog(float start, float end, float r, float g, float b, float a, String shape) {
        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(end);
        RenderSystem.setShaderFogColor(r, g, b, a);
        FogShape fogShape = shape.equalsIgnoreCase("sphere") ? FogShape.SPHERE : FogShape.CYLINDER;
        RenderSystem.setShaderFogShape(fogShape);
    }

    @LuaExportMethod
    public static void setShaderLights(float x1, float y1, float z1, float x2, float y2, float z2) {
        RenderSystem.setShaderLights(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2));
    }

    @LuaExportMethod
    public static void setShaderGameTime(long time, float tickDelta) {
        RenderSystem.setShaderGameTime(time, tickDelta);
    }

    @LuaExportMethod
    public static float getShaderGameTime() {
        return RenderSystem.getShaderGameTime();
    }

    @LuaExportMethod
    public static void lineWidth(float width) {
        RenderSystem.lineWidth(width);
    }

    @LuaExportMethod
    public static float getShaderLineWidth() {
        return RenderSystem.getShaderLineWidth();
    }

    @LuaExportMethod
    public static void enableBlend() {
        RenderSystem.enableBlend();
    }

    @LuaExportMethod
    public static void disableBlend() {
        RenderSystem.disableBlend();
    }

    @LuaExportMethod
    public static void defaultBlendFunc() {
        RenderSystem.defaultBlendFunc();
    }

    @LuaExportMethod
    public static void blendFunc(int srcFactor, int dstFactor) {
        RenderSystem.blendFunc(srcFactor, dstFactor);
    }

    @LuaExportMethod
    public static void blendFuncSeparate(int srcFactorRGB, int dstFactorRGB, int srcFactorAlpha, int dstFactorAlpha) {
        RenderSystem.blendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha);
    }

    @LuaExportMethod
    public static void blendEquation(int mode) {
        RenderSystem.blendEquation(mode);
    }

    @LuaExportMethod
    public static void enableDepthTest() {
        RenderSystem.enableDepthTest();
    }

    @LuaExportMethod
    public static void disableDepthTest() {
        RenderSystem.disableDepthTest();
    }

    @LuaExportMethod
    public static void depthFunc(int func) {
        RenderSystem.depthFunc(func);
    }

    @LuaExportMethod
    public static void depthMask(boolean mask) {
        RenderSystem.depthMask(mask);
    }

    @LuaExportMethod
    public static void enableCull() {
        RenderSystem.enableCull();
    }

    @LuaExportMethod
    public static void disableCull() {
        RenderSystem.disableCull();
    }

    @LuaExportMethod
    public static void enableScissor(int x, int y, int width, int height) {
        RenderSystem.enableScissor(x, y, width, height);
    }

    @LuaExportMethod
    public static void disableScissor() {
        RenderSystem.disableScissor();
    }

    @LuaExportMethod
    public static void enablePolygonOffset() {
        RenderSystem.enablePolygonOffset();
    }

    @LuaExportMethod
    public static void disablePolygonOffset() {
        RenderSystem.disablePolygonOffset();
    }

    @LuaExportMethod
    public static void polygonOffset(float factor, float units) {
        RenderSystem.polygonOffset(factor, units);
    }

    @LuaExportMethod
    public static void enableColorLogicOp() {
        RenderSystem.enableColorLogicOp();
    }

    @LuaExportMethod
    public static void disableColorLogicOp() {
        RenderSystem.disableColorLogicOp();
    }

    @LuaExportMethod
    public static void logicOp(int op) {
        RenderSystem.logicOp(GlStateManager.LogicOp.values()[op]);
    }

    @LuaExportMethod
    public static void polygonMode(int face, int mode) {
        RenderSystem.polygonMode(face, mode);
    }

    @LuaExportMethod
    public static void activeTexture(int texture) {
        RenderSystem.activeTexture(texture);
    }

    @LuaExportMethod
    public static void bindTexture(int textureId) {
        RenderSystem.bindTexture(textureId);
    }

    @LuaExportMethod
    public static void deleteTexture(int textureId) {
        RenderSystem.deleteTexture(textureId);
    }

    @LuaExportMethod
    public static void setShaderTexture(int textureUnit, int textureId) {
        RenderSystem.setShaderTexture(textureUnit, textureId);
    }

    @LuaExportMethod
    public static int getShaderTexture(int textureUnit) {
        return RenderSystem.getShaderTexture(textureUnit);
    }

    @LuaExportMethod
    public static void texParameter(int target, int pname, int param) {
        RenderSystem.texParameter(target, pname, param);
    }

    @LuaExportMethod
    public static void viewport(int x, int y, int width, int height) {
        RenderSystem.viewport(x, y, width, height);
    }

    @LuaExportMethod
    public static void clearColor(float r, float g, float b, float a) {
        RenderSystem.clearColor(r, g, b, a);
    }

    @LuaExportMethod
    public static void clearDepth(double depth) {
        RenderSystem.clearDepth(depth);
    }

    @LuaExportMethod
    public static void clearStencil(int stencil) {
        RenderSystem.clearStencil(stencil);
    }

    @LuaExportMethod
    public static void clear(int mask) {
        RenderSystem.clear(mask, false);
    }

    @LuaExportMethod
    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        RenderSystem.colorMask(red, green, blue, alpha);
    }

    @LuaExportMethod
    public static void stencilFunc(int func, int ref, int mask) {
        RenderSystem.stencilFunc(func, ref, mask);
    }

    @LuaExportMethod
    public static void stencilMask(int mask) {
        RenderSystem.stencilMask(mask);
    }

    @LuaExportMethod
    public static void stencilOp(int sfail, int dpfail, int dppass) {
        RenderSystem.stencilOp(sfail, dpfail, dppass);
    }

    @LuaExportMethod
    public static void setProjectionMatrix(float[] matrix) {
        if (matrix.length == 16) {
            Matrix4f mat = new Matrix4f();
            mat.set(matrix);
            RenderSystem.setProjectionMatrix(mat, RenderSystem.getVertexSorting());
        }
    }

    @LuaExportMethod
    public static void setTextureMatrix(float[] matrix) {
        if (matrix.length == 16) {
            Matrix4f mat = new Matrix4f();
            mat.set(matrix);
            RenderSystem.setTextureMatrix(mat);
        }
    }

    @LuaExportMethod
    public static void resetTextureMatrix() {
        RenderSystem.resetTextureMatrix();
    }

    @LuaExportMethod
    public static void setInverseViewRotationMatrix(float[] matrix) {
        if (matrix.length == 9) {
            Matrix3f mat = new Matrix3f();
            mat.set(matrix);
            RenderSystem.setInverseViewRotationMatrix(mat);
        }
    }

    @LuaExportMethod
    public static void backupProjectionMatrix() {
        RenderSystem.backupProjectionMatrix();
    }

    @LuaExportMethod
    public static void restoreProjectionMatrix() {
        RenderSystem.restoreProjectionMatrix();
    }

    @LuaExportMethod
    public static void pixelStore(int pname, int param) {
        RenderSystem.pixelStore(pname, param);
    }

    @LuaExportMethod
    public static int maxSupportedTextureSize() {
        return RenderSystem.maxSupportedTextureSize();
    }

    @LuaExportMethod
    public static String getApiDescription() {
        return RenderSystem.getApiDescription();
    }

    @LuaExportMethod
    public static String getCapsString() {
        return RenderSystem.getCapsString();
    }

    @LuaExportMethod
    public static void drawElements(int mode, int count, int type) {
        RenderSystem.drawElements(mode, count, type);
    }

    @LuaExportMethod
    public static void setColor(float r, float g, float b, float a) {
        setShaderColor(r, g, b, a);
    }

    @LuaExportMethod
    public static void startRender() {
        GraphicsSystem.begin();
    }

    @LuaExportMethod
    public static void endRender() {
        GraphicsSystem.end();
    }

    @LuaExportMethod
    public static void setupGui3DDiffuseLighting(float x1, float y1, float z1, float x2, float y2, float z2) {
        RenderSystem.setupGui3DDiffuseLighting(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2));
    }

    @LuaExportMethod
    public static void setupGuiFlatDiffuseLighting(float x1, float y1, float z1, float x2, float y2, float z2) {
        RenderSystem.setupGuiFlatDiffuseLighting(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2));
    }

    @LuaExportMethod
    public static void setupLevelDiffuseLighting(float x1, float y1, float z1, float x2, float y2, float z2, float[] matrix) {
        if (matrix.length == 16) {
            Matrix4f mat = new Matrix4f();
            mat.set(matrix);
            RenderSystem.setupLevelDiffuseLighting(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), mat);
        }
    }

    @LuaExportMethod
    public static void defaultDepthFunc() {
        depthFunc(515); // GL_LEQUAL
    }

    @LuaExportMethod
    public static void lessDepthFunc() {
        depthFunc(513); // GL_LESS
    }

    @LuaExportMethod
    public static void alwaysDepthFunc() {
        depthFunc(519); // GL_ALWAYS
    }

    @LuaExportMethod
    public static void neverDepthFunc() {
        depthFunc(512); // GL_NEVER
    }

    @LuaExportMethod
    public static int getBlendZero() { return 0; } // GL_ZERO
    @LuaExportMethod
    public static int getBlendOne() { return 1; } // GL_ONE
    @LuaExportMethod
    public static int getBlendSrcColor() { return 768; } // GL_SRC_COLOR
    @LuaExportMethod
    public static int getBlendOneMinusSrcColor() { return 769; } // GL_ONE_MINUS_SRC_COLOR
    @LuaExportMethod
    public static int getBlendDstColor() { return 774; } // GL_DST_COLOR
    @LuaExportMethod
    public static int getBlendOneMinusDstColor() { return 775; } // GL_ONE_MINUS_DST_COLOR
    @LuaExportMethod
    public static int getBlendSrcAlpha() { return 770; } // GL_SRC_ALPHA
    @LuaExportMethod
    public static int getBlendOneMinusSrcAlpha() { return 771; } // GL_ONE_MINUS_SRC_ALPHA
    @LuaExportMethod
    public static int getBlendDstAlpha() { return 772; } // GL_DST_ALPHA
    @LuaExportMethod
    public static int getBlendOneMinusDstAlpha() { return 773; } // GL_ONE_MINUS_DST_ALPHA

    @LuaExportMethod
    public static int getDepthNever() { return 512; } // GL_NEVER
    @LuaExportMethod
    public static int getDepthLess() { return 513; } // GL_LESS
    @LuaExportMethod
    public static int getDepthEqual() { return 514; } // GL_EQUAL
    @LuaExportMethod
    public static int getDepthLequal() { return 515; } // GL_LEQUAL
    @LuaExportMethod
    public static int getDepthGreater() { return 516; } // GL_GREATER
    @LuaExportMethod
    public static int getDepthNotEqual() { return 517; } // GL_NOTEQUAL
    @LuaExportMethod
    public static int getDepthGequal() { return 518; } // GL_GEQUAL
    @LuaExportMethod
    public static int getDepthAlways() { return 519; } // GL_ALWAYS

    @LuaExportMethod
    public static int getDrawPoints() { return 0; } // GL_POINTS
    @LuaExportMethod
    public static int getDrawLines() { return 1; } // GL_LINES
    @LuaExportMethod
    public static int getDrawLineLoop() { return 2; } // GL_LINE_LOOP
    @LuaExportMethod
    public static int getDrawLineStrip() { return 3; } // GL_LINE_STRIP
    @LuaExportMethod
    public static int getDrawTriangles() { return 4; } // GL_TRIANGLES
    @LuaExportMethod
    public static int getDrawTriangleStrip() { return 5; } // GL_TRIANGLE_STRIP
    @LuaExportMethod
    public static int getDrawTriangleFan() { return 6; } // GL_TRIANGLE_FAN
    @LuaExportMethod
    public static int getDrawQuads() { return 7; } // GL_QUADS

    @LuaExportMethod
    public static int getClearColorBit() { return 16384; } // GL_COLOR_BUFFER_BIT
    @LuaExportMethod
    public static int getClearDepthBit() { return 256; } // GL_DEPTH_BUFFER_BIT
    @LuaExportMethod
    public static int getClearStencilBit() { return 1024; } // GL_STENCIL_BUFFER_BIT
    @LuaExportMethod
    public static int getClearAll() { return 17664; } // Все три выше

    @LuaExportMethod
    public static int getTexture2D() { return 3553; } // GL_TEXTURE_2D
    @LuaExportMethod
    public static int getTextureCubeMap() { return 34067; } // GL_TEXTURE_CUBE_MAP

    @LuaExportMethod
    public static int getTexMinFilter() { return 10241; } // GL_TEXTURE_MIN_FILTER
    @LuaExportMethod
    public static int getTexMagFilter() { return 10240; } // GL_TEXTURE_MAG_FILTER
    @LuaExportMethod
    public static int getTexWrapS() { return 10242; } // GL_TEXTURE_WRAP_S
    @LuaExportMethod
    public static int getTexWrapT() { return 10243; } // GL_TEXTURE_WRAP_T

    @LuaExportMethod
    public static int getTexFilterNearest() { return 9728; } // GL_NEAREST
    @LuaExportMethod
    public static int getTexFilterLinear() { return 9729; } // GL_LINEAR
    @LuaExportMethod
    public static int getTexWrapRepeat() { return 10497; } // GL_REPEAT
    @LuaExportMethod
    public static int getTexWrapClamp() { return 10496; } // GL_CLAMP
}