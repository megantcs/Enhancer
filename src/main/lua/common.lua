-- standart library by @dx33
-- since version 1.0 available the preprocessor
-- use #include common.lua
-- extension feature:
--   #pragma LE_COMMAND_DUMP : dump, dump of code with expanded preprocessor directives. 
--                           after specifying this directive, a file 'le_dump.lua.dump' 
--                           with the dump will be created in the root folder.


#ifndef COMMON_LUA
#define COMMON_LUA

#define minecraft getMinecraft()

-- minecraft block

function getMinecraft() 
    return Minecraft.getInstance();
end

function getHealth()
    if minecraft.getPlayer() == nil 
        then return -1;
    end
    return minecraft.getPlayer().getHealth();
end

function getUsername()
    if minecraft.getPlayer() == nil 
        then return "(null)";
    end
    return minecraft.getPlayer().getName();
end

function getDisplayname()
    if minecraft.getPlayer() == nil 
        then return "(null)";
    end
    return minecraft.getPlayer().getDisplayName();
end


-- render block

#define renderObject getRenderObject()
#define COLOR_4_HEX_SET hex1, hex2, hex3, hex4
#define BREAK_IF_RENDER_NIL if renderObject == nil \
                                then return; \
                            end

function getRenderObject()
    return RenderUtil.getRenderObject();
end

function drawBlur(x, y, z, width, height, radius, alpha, blur, COLOR_4_HEX_SET)
    BREAK_IF_RENDER_NIL
    renderObject.drawBlurLC(x, y, z, height, width, radius, alpha, blur, COLOR_4_HEX_SET);
end

function drawRect(x, y, z, width, height, radius, alpha, COLOR_4_HEX_SET)
    BREAK_IF_RENDER_NIL
    renderObject.drawRectLC(x, y, z, height, width, radius, alpha, COLOR_4_HEX_SET);
end

function drawEffect(x, y, z, width, height, radius, COLOR_4_HEX_SET) 
    BREAK_IF_RENDER_NIL
    renderObject.drawEffectLC(x, y, z, width, height, radius, COLOR_4_HEX_SET);
end

function drawImage(filename, x0, y0,width, height, u, v,
                          regionWidth, regionHeight, textureWidth, 
                          textureHeight, COLOR_4_HEX_SET)
    BREAK_IF_RENDER_NIL
    renderObject.drawImageLC(filename, x0, y0, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight, COLOR_4_HEX_SET);
end

function drawFont(fontPath, x, y, text, hex)
    BREAK_IF_RENDER_NIL
    renderObject.drawFontLC(fontPath, x, y, text, hex);
end

function drawText(x, y, text, hex, shadow)
    BREAK_IF_RENDER_NIL
    renderObject.drawMCFontLC(x, y, text, hex, shadow);
end

-- work with buffers.
--
-- exmaple code:
--   local buffer = getBuffer();
--   buffer.begin(0,0);
--   buffer.vertex(x, y, z, hex);
--   drawBuffer(); // if not  called, then critical crash. 'Alreadly buffer'
--

function drawBuffer()
    RenderUtil.draw();
end

function getBuffer()
    return RenderUtil.getBuffer();
end


#endif
