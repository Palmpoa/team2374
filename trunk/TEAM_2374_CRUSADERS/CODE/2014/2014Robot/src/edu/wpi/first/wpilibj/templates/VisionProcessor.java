/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author robotics
 */
public class VisionProcessor {
    AxisCamera camera;
    public VisionProcessor(){
        //gets the camera so we can process images
        camera=AxisCamera.getInstance();
    }
    public boolean isHot(){
        int rectTolerance=40;//higher means less tolerant
        int aspectTolerance=80;//lower means less tolerant
        try {
            boolean hot=false;
            //gets an image
            ColorImage image=camera.getImage();
            //creates an image that only consists of the green-led-color pixels
            BinaryImage threshold=image.thresholdRGB(180,230,220,255,200,255);
            //gets rid of small things
            BinaryImage filter=threshold.removeSmallObjects(false, 1);
            
            for(int i=0; i<filter.getNumberParticles()&&i<8; i++){
                //gets each particle (group of pixels) from the image
                ParticleAnalysisReport report=filter.getParticleAnalysisReport(i);
                //checks if the area of the particle is somewhat rectangular
                if(report.particleArea*100>(report.boundingRectHeight*report.boundingRectWidth)*rectTolerance){
                    //computes the width to height ratio of the particle
                    double aspectRatio=(double)report.boundingRectWidth/(double)report.boundingRectHeight;
                    double targetRatio=23.5/4;//the ratio of the actual goal
                    //checks whether or not the rectangle's ratio is relatively close
                    if(aspectRatio*(100+aspectTolerance)>targetRatio*100&&aspectRatio*100<targetRatio*(100+aspectTolerance)){
                        //it must be hot!
                        hot=true;
                    }
                }
            }
            //frees up the memory of the cRIO
            image.free();
            threshold.free();
            filter.free();
            return hot;
            
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
