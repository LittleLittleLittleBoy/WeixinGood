from PIL import Image

count=1
img = Image.open('上一步的截图路径')
for i in range(0,5):
    for j in range(0,6):
        region=(192*i,269*j,192*i+141,269*j+141)
        cropImg = img.crop(region)
        cropImg.save('存储路径/p'+str(count)+'.png')
        count+=1

