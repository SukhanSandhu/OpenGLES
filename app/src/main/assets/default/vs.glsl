#version 300 es

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexPosition;

uniform mat4 uMVPMatrix;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;


out vec2 vTexPosition;

void main() {
    gl_Position =vec4(aPos, 1.0, 1.0);
    vTexPosition = aTexPosition;
}