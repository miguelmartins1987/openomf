#version 300 es
precision highp float;
precision highp int;

in vec2 tex_coord;
uniform sampler2D palette;
uniform highp usampler2D atlas;

layout (location = 0) out vec4 color;

void main() {
    uvec4 texel = texture(atlas, tex_coord);
    ivec2 index = ivec2(int(texel.r), 0);
    color = texelFetch(palette, index, 0);
}