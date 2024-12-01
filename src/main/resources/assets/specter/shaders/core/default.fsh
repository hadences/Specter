#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }

//    color = vec4(1.0, 0.0, 0.0, 1.0);

    fragColor = color;
}
