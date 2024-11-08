#version 150

#moj_import <fog.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

out float vertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;

void main() {
    vec4 position = ModelViewMat * vec4(Position, 1.0);

    // make positions wavy
//    position.z += sin(position.x * 5.0);

    position = ProjMat * position;

    gl_Position = position;

    vertexDistance = fog_distance(Position, FogShape);
    texCoord0 = UV0;
    vertexColor = Color * texelFetch(Sampler0, UV2 / 16, 0);
}
