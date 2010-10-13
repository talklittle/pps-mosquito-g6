package mosquito.g6;
import java.awt.geom.*;
import java.util.*;

public class ArrangedAreasAndSizes {

	Set<Set<Line2D>> areas;
	Set<Double> sizes;
	
	public ArrangedAreasAndSizes(Set<Set<Line2D>> allAreas){
		areas = allAreas;
		sizes = new HashSet<Double>();
		getSizes();
		//arrange();
	}
	
	//wrong
	private void arrange(){
		int index = sizes.size();
		double tempSize = 0.0,maxSize=0.0;
		Iterator<Double> sizeIterator = sizes.iterator();
		Set<Line2D> tempArea,maxArea;
		Iterator<Set<Line2D>> areaIterator = areas.iterator();
		for(int i = 0;i<sizes.size();i++){
			sizeIterator = sizes.iterator();
			areaIterator = areas.iterator();
			maxSize = sizeIterator.next();
			maxArea = areaIterator.next();
			for(int j=1;j<=index;j++){
				tempSize = sizeIterator.next();
				tempArea = areaIterator.next();
				if(maxSize<tempSize){
					maxSize = tempSize;
					maxArea = tempArea;
				}
			}
			sizes.remove(maxSize);
			sizes.add(maxSize);
			areas.remove(maxArea);
			areas.add(maxArea);
			index--;
		}
	}
	
	private void getSizes(){
		Iterator<Set<Line2D>> iteratorAll = areas.iterator();
		Iterator<Line2D> iteratorEach;
		Line2D line;
		Set<Line2D> lines;
		boolean first = true;
		double x1=0,y1=0,x2=0,y2=0,firstX=0,firstY=0,size=0;
		while(iteratorAll.hasNext()){
			first = true;
			lines = (Set<Line2D>)iteratorAll.next();
			iteratorEach = lines.iterator();
			x1=0;
			y1=0;
			x2=0;
			y2=0;
			firstX=0;
			firstY=0;
			size=0;
			//surveyor's formula to find area. >> http://mathforum.org/library/drmath/view/55141.html
			while(iteratorEach.hasNext()){
				line = (Line2D)iteratorEach.next();
				if(first){
					firstX = line.getX1();
					firstY = line.getY1();
					first = false;
				}
				x1 = x2;
				y1 = y2;
				x2 = line.getX1();
				y2 = line.getY1();
				size += x1*y2-x2*y1;
				x1 = x2;
				y1 = y2;
				x2 = line.getX2();
				y2 = line.getY2();
				size += x1*y2-x2*y1;
			}
			x1 = x2;
			y1 = y2;
			x2 = firstX;
			y2 = firstY;
			size += x1*y2-x2*y1;
			sizes.add(size);
		}
	}
	
}
