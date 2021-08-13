#version 300 es

out vec4 FragColor;

uniform sampler2D uTexture;
in vec2 vTexPosition;

void main() {

  FragColor = texture(uTexture, vTexPosition);

}