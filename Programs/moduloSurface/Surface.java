package moduloSurface;

import Methods.Fun;

public class Surface {
	Feature[] features;
	int numFeatures = 0;
	int numMaxFeatures;
	int surfaceSize;
	
	Surface(int surfaceSize){
		numMaxFeatures = surfaceSize*surfaceSize;
		features = new Feature[numMaxFeatures];
		this.surfaceSize = surfaceSize;
	}
	
	Feature addFeatureAt(int x, int y, String name){
		Feature thisFeature = new Feature(x, y, name);
		features[numFeatures] = thisFeature;
		numFeatures = numFeatures + 1;
		return thisFeature;
	}
	
	Feature newFeatureSomewhere(String name){
		if(numFeatures >= numMaxFeatures-3){
			return null;
		}
		int x, y;
		do{
			x = Fun.rng(surfaceSize);
			y = Fun.rng(surfaceSize);
		}while(isFeatureAt(x, y));
		
		Feature thisFeature = new Feature(x, y, name);
		features[numFeatures] = thisFeature;
		numFeatures = numFeatures + 1;
		return thisFeature;
		
	}
	
	void newFeatureSomewhereMult(String name, int quantity){
		for(int i = 0; i < quantity; i++){
			newFeatureSomewhere(name);
		}
	}
	
	boolean isFeatureAt(int testX, int testY){
		for(int i = 0; i < numFeatures; i++){
			if(features[i].isAt(testX, testY)){
				return true;
			}
		}
		return false;
	}
	
	Feature featureAt(int testX, int testY){
		for(int i = 0; i < numFeatures; i++){
			if(features[i].isAt(testX, testY)){
				return features[i];
			}
		}
		return null;
	}
	
	Feature featureAtMod(int testX, int testY){
		return featureAt(Fun.positiveMod(testX, surfaceSize), Fun.positiveMod(testY, surfaceSize));
	}
}
