/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.ResDigest;

import java.util.*;
public class Mapper {

	private String plasmidString;
	private String annotatedPlasmidString="";
	//result will hold tuples : (site position, enzyme abbrv)
	ArrayList<Tuple> result = new ArrayList<Tuple>();
	ArrayList<Integer> locations = null;
	
	public static void main(String[] args)
	{
		String plasmid = "AguighiugiuARISHIGANGULYdogufefegeTINAgegr2r3re";
		Mapper mapper = new Mapper(plasmid);
		ArrayList<Integer>a;
		a = mapper.markRestrictionSites("RISHI*GANGoLY", "(rg)");
		//a = mapper.markRestrictionSites("TINA (2/1)","(tg)");
		
		System.out.println(mapper.getResult());
		System.out.println(mapper.getPlasmidString());
		System.out.println(mapper.getAnnotatedPlasmidString());
		ArrayList<String> b = mapper.getFragments(true);
		

               /** ArrayList<String> e = new ArrayList<String>();
                e.add("ABCD"); e.add("RIS");

                ArrayList<String> m = new ArrayList<String>();
                m.add("(E1)"); m.add("(B2)");
                ArrayList<String> out = new ArrayList<String>();
                ArrayList<String> allLocations = new ArrayList<String>();
                Mapper opt = getOptimalMap(plasmid,e,m,0,out,allLocations);
                System.out.println(out);
                **/


	}

	public Mapper(String s)
	{
		this.plasmidString = s;
		this.annotatedPlasmidString = new String(this.plasmidString);
	    locations = new ArrayList<Integer>();
	}
	public String getPlasmidString()
	{
		return this.plasmidString;
	}

	public ArrayList<Tuple> getResult()
	{
		return this.result;
	}

	private void sort()
	{

		for (int i = 0 ; i < result.size() -1 ; i ++)
		{
			Tuple minTuple = result.get(i);
			for (int j = i+1; j < result.size(); j++)
			{
				Tuple current = result.get(j);
				if (minTuple.position > current.position)
				{
					result.set(i, current);
					result.set(j, minTuple);
					minTuple = current;
				}

			}
		}
	}
        
        
        private void sortDescString(ArrayList<String> a)
        {
            for (int i =0; i < a.size()-1; i++)
		{
			int max = a.get(i).length();
			for (int j=i+1; j <a.size();j++)
			{
				int current = a.get(j).length();
				if (max < current)
				{
					String temp = a.get(j);
                                        a.set(j, a.get(i));
                                        a.set(i, temp);
				}
			}
		}
            
        }
	private void sortDesc(ArrayList<Integer> a)
	{
		for (int i =0; i < a.size()-1; i++)
		{
			int max = a.get(i).intValue();
			for (int j=i+1; j <a.size();j++)
			{
				int current = a.get(j).intValue();
				if (max < current)
				{
					a.set(i, new Integer(current));
					a.set(j, new Integer(max));
				}
			}
		}
	}
	public String getAnnotatedPlasmidString()
	{
		//first SORT the site positions, then MARK the positions with the enzyme
		//abbreviations in DESCENDING order.

		for (int i = result.size()-1; i >=0; i --)
		{
			Tuple t = result.get(i);
			this.annotatedPlasmidString = this.annotatedPlasmidString.substring(0,t.position)+t.abbr+this.annotatedPlasmidString.substring(t.position);
		}
		return this.annotatedPlasmidString;
	}
	/**
	 * Looks for the restriction sites ( as given by parameter substr)
	 * and marks the places for "cutting" with the given token
	 * @param token: the marker used to point where the enzyme will cut
	 * @param substr: Restriction site DNA piece for the enzyme used to cut
	 * general pattern : (num/num) ACGGTTNNNCCGG (num/num)
	 * @return list of restriction sites as positions
	 */
	public ArrayList<Integer> markRestrictionSites(String substr, String token)
	{
		
       ArrayList<Integer>currentSites = new ArrayList<Integer>();
        String pattern;
        String forward=null;String backward=null;
        String[] w = substr.split("\\s+");
        if (w.length ==3 ){backward=w[0];pattern=w[1];forward=w[2];}
        else if (w.length ==2) {pattern =w[0]; forward = w[1];}
        else pattern = w[0];
        //find the cutsite postition marked in the enzyme
        int cutsite = 0;
        for (int i=0; i < pattern.length();i++)
        {
        	if (pattern.charAt(i)=='*'){ cutsite = i;break;}
        }
        pattern = pattern.replaceAll("\\*", "");
        String regex = pattern.replaceAll("N", "[a-zA-Z]");
		for (int i = 0 ; i < this.plasmidString.length() - pattern.length()+1; i++)
		{

			if (plasmidString.substring(i,i+pattern.length()).matches(regex))
			{
				if (cutsite > 0)
				{
					int position = i+cutsite-1;
					locations.add(new Integer(position));
					currentSites.add(new Integer(position));
				}
				if (forward !=null)
				{
					String[] p = forward.substring(1, forward.length()-2).split("/");
					int position =Integer.parseInt(p[0])+ i+ pattern.length()-1;
					locations.add(new Integer(position));
					currentSites.add(new Integer(position));
				}
				if (backward !=null)
				{
					String[] p = backward.substring(1, backward.length()-2).split("/");
					int position =i-Integer.parseInt(p[0])-1;
					locations.add(new Integer(position));
					currentSites.add(new Integer(position));
				}
               // locations.add(new Integer(i));
				//record the position of the cutting site along with enzyme abbrv

				result.add(new Tuple(i,token));
				//mark with the token string where the DNA will be cut by the restriction enzyme
				//this.annotatedPlasmidString= this.annotatedPlasmidString.substring(0, i)+token+this.plasmidString.substring(i);
				//i+=substr.length()+1;
			}
		}
		sort();
		return currentSites;
	}

	
	/**
	 * the new method that gets the DNA fragments after being cut with restriction enzymes
	 * 
	 * @param circular : if this is a circular dna or linear
	 * @return arraylist of fragments of dna string after being digested by enzymes
	 */
	public ArrayList<String> getFragments(boolean circular)
	{
		ArrayList<String>frags = new ArrayList<String>();
		sortDesc(this.locations);
		String bstring = new String(this.plasmidString);
		for (int i = 0; i < this.locations.size(); i++)
		{
			int index = locations.get(i).intValue();
			//insert a * to mark the cut
			
			bstring = bstring.substring(0, index +1)+"*"+bstring.substring(index+1);
		}
		System.out.println("bstring = "+ bstring);
		
		if (!circular)
		{
			String []f = bstring.split("\\*");
			for (int i =0; i < f.length; i++)
                        {
                                if (f[i].length() >0)
				frags.add(f[i]);
                        }
		}
		else 
		{
			String []f =bstring.split("\\*");
			if (f.length > 1)
			{
				f[0] = f[f.length-1]+f[0];
				for (int i = 0; i <f.length -1; i++)
				{
                                        if (f[i].length() >0)
					frags.add(f[i]);
					
				}
			}
		}
		sortDescString(frags);
		return frags;
		
	}
	/**DEPCRECATED method
	 * displays the DNA fragments and the length of each fragment
	 * after the plasmid is cut with the Restriction enzymes
	 * @param fragPos : arraylist of restriction site positions for enzymes on the plasmid
	 * @return arraylist of  the cut DNA fragments
	 */
	public ArrayList<String> getFragments()
	{
		ArrayList<String> frags = new ArrayList<String>();
                if (result.size()==0) return frags;
		for (int i = 0 ; i <result.size()-1 ; i ++)
		{
			Tuple begin = result.get(i); Tuple end = result.get(i+1);
			int beginIndex = begin.position;
			int endIndex = end.position;
			String frag = this.plasmidString.substring(beginIndex, endIndex);
			//System.out.println(frag+"\nSize = "+frag.length());
			frags.add(frag);
		}

		/** since the plasmid is circular, the last fragment will be from the last
		 * cut site to the first cut site!
		 *
		 */
		Tuple last = result.get(result.size() -1 ); Tuple first = result.get(0);
		String lastPart = this.plasmidString.substring(last.position);
		String firstPart = this.plasmidString.substring(0,first.position);
		String lastFrag = lastPart+firstPart;
		//System.out.println(lastFrag+"\nSize = "+lastFrag.length());
		frags.add(lastFrag);

		return frags;
	}


	/**
	 * this method will find the optimal enzymes to use to cut the plasmid.
	 * Optimal in the sense that the minimal difference between the cut segment is max.
	 *
	 * @param enzymes: list of enzymes to consider. So we need to consider all possible subsets of
	 * enzymes in the list.
	 * @return ArrayList of optimal enzymes.
	 */
	public static ArrayList findOptimalEnzymes(ArrayList<String> enzymes)
	{

		return null;
	}
	
        private static int getLeastLength(ArrayList<String> a)
        {
            if (a.size() ==0) return 0;
            int min = a.get(0).length();
            for (int i =0; i < a.size() ; i++)
            {
                if (a.get(i).length() < min) min = a.get(i).length();
            }
            return min;
        }


        private static int findMinDistance(ArrayList<String> frags)
        {
        	//find minimum difference between length of any 2 fragments
        	if (frags.size() <= 1) return 0;
        	//System.out.println("frags: " +frags );
            int min = frags.get(0).length();
            for (int j =0; j < frags.size(); j++)
            {
                for (int k = 0; k < frags.size(); k++)
                {
                	if ( k ==j )continue;
                    int diff = Math.abs(frags.get(k).length() - frags.get(j).length());
                    if (diff  < min) min = diff;
                }
            }
            return min;
          }

        public static Mapper getOptimalMap(String plasmid, ArrayList<String> enz, ArrayList<String> markers,int lowerBound, ArrayList<String>outputEnzymes,ArrayList<String> allLocations)
        {
            Mapper optimalMap = null;
            ArrayList<String> optimalEnzymes=null;
            int minDistance = -1;
            //first, check with cleaving with one enzyme at a time
            for (int i =0; i < enz.size(); i++)
            {
                Mapper mapper = new Mapper(plasmid);
                ArrayList<Integer> locations = mapper.markRestrictionSites(enz.get(i), markers.get(i));
                ArrayList<String> frags = mapper.getFragments();
                if (getLeastLength(frags) < lowerBound)
                    continue;
                //find minimum difference between length of any 2 fragments
                int n = findMinDistance(frags);
               // System.out.println("n = "+n);
                if ( n > minDistance)
                {
                	minDistance = n;
                	for (int z=0; z < outputEnzymes.size();z++) outputEnzymes.remove(z);
                	outputEnzymes.add(markers.get(i));
                        
                        for (int z=0; z < allLocations.size();z++) allLocations.remove(z);
                	allLocations.add(locations.toString());
                	optimalMap = mapper;
                }
            }
                // now check with 2 enzymes cutting together
            for (int i =0; i < enz.size(); i++)
            {
                for (int j = 0; j < enz.size();j++)
                {
                	if (i == j ) continue;
                	Mapper mapper = new Mapper(plasmid);
                    ArrayList<Integer> locationsI = mapper.markRestrictionSites(enz.get(i), markers.get(i));
                	ArrayList<Integer> locationsJ = mapper.markRestrictionSites(enz.get(j), markers.get(j));
                	//System.out.println(mapper.getAnnotatedPlasmidString());
                	ArrayList<String> frags = mapper.getFragments();
                	if (getLeastLength(frags) < lowerBound)
                		continue;
                	int n= findMinDistance(frags);
                	//System.out.println("n = "+n);
                	if ( n > minDistance)
                	{
                		optimalMap = mapper;
                		minDistance = n;
                		for (int z=0; z < outputEnzymes.size();z++) outputEnzymes.remove(z);
                		outputEnzymes.add(markers.get(i));outputEnzymes.add(markers.get(j));
                                
                                for (int z=0; z < allLocations.size(); z++) allLocations.remove(z);
                                allLocations.add(locationsI.toString());allLocations.add(locationsJ.toString());
                	}

                }
            }

       return optimalMap;
    }


	class Tuple {
		public String abbr="";
		public int position = -1;

		public Tuple(int pos, String a)
		{
			this.position = pos; this.abbr = a;
		}

        @Override
		public String toString()
		{
			return "( "+this.position+" , "+this.abbr+")";
		}


	}
}