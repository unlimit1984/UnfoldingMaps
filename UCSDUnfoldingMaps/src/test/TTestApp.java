package test;

import demos.Airport;

public class TTestApp {
	public static void main(String[] args) {

		

	}
	public static String findAirportCodeBS(String toFind, Airport[] airports){
		int low = 0;
		int high = airports.length - 1;
		int mid;
		while(low<=high){
//			mid = (low+high)/2;// вдруг будет большой integer, лучше писать mid = low+ (high-low)/2;
			mid = low+ (high-low)/2;
			int compare = toFind.compareTo(airports[mid].getCity());
			if(compare < 0){
				high = mid-1;
			}
			else if(compare > 0){
				low = mid+1;
			}
			else{
				return airports[mid].getCode3();
			}
			
		}
		return null;
	}
}
