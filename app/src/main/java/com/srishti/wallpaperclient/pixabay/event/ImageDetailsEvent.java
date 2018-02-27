package com.srishti.wallpaperclient.pixabay.event;


import com.srishti.wallpaperclient.pixabay.model.Image;

import java.util.List;



public class ImageDetailsEvent {

    private Image image;
    private  int position;
    private List<Image> imageList;

    public List<Image> getImageList() {
        return imageList;
    }

    public ImageDetailsEvent(Image image,int position, List<Image> imageList) {
        this.image = image;
        this.position=position;

        this.imageList = imageList;
    }

    public Image getImage() {
        return image;
    }

    public int getPosition() {
        return position;
    }

}
