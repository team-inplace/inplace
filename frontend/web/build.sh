#!/bin/sh

# 기존 output 디렉토리가 있다면 삭제
rm -rf ../output

# output 디렉토리 생성 (frontend의 상위 디렉토리에)
mkdir -p ../output

# 프로젝트 구조를 output으로 복사
cp -R ../.github ../.hooks ../backend ../frontend ../.gitignore ../output/

# frontend 디렉토리의 최신 내용으로 업데이트
cp -R * ../output/frontend/

# 디버깅을 위한 디렉토리 구조 출력
echo "Output directory structure:"
ls -la ../output/