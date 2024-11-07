#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
//    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
    vec2 uv = texCoord0;
    vec4 texColor = texture(Sampler0, uv);

//    float strength = abs(distance(uv, vec2(0.25)));
//    color = vec4(strength, strength, strength, 1.0);
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
