package com.abdulelah.binsaleem.helpers;

import com.abdulelah.binsaleem.models.raw_data.Query;

import java.util.*;

public class QueryHelper {

    public static List<Query> CountryReports;
    public static List<Query> CityReports;
    public static List<Query> CapitalCityReports;
    public static List<Query> PopulationReports;
    
    //Holds pre-defined SQL queries that can be run to retried data for Report generation
    public QueryHelper() {
        CountryReports = new ArrayList<Query>(Arrays.asList(new Query(
                "SELECT CO.name,CO.code,CO.continent,CO.region,CO.population,CI.name as capital FROM country CO LEFT JOIN city CI ON CO.capital = CI.id ORDER BY population DESC;",
                "All the countries in the world organised by largest population to smallest.")
        ));

        CityReports = new ArrayList<Query>(Arrays.asList(new Query(
                "SELECT CI.Name, CO.Name as country, CI.District, CI.Population FROM city CI LEFT JOIN country CO ON CI.countrycode = CO.code ORDER BY CI.population DESC;",
                "All the cities in the world organised by largest population to smallest."),
                new Query(
                        "SELECT CI.Name, CO.Name as country, CI.District, CI.Population FROM city CI LEFT JOIN country CO ON CI.countrycode = CO.code ORDER BY CI.population DESC LIMIT 5",
                        "The top N populated cities in the world where N is provided by the user.")
        ));

        CapitalCityReports = new ArrayList<Query>(Arrays.asList(
                new Query(
                        "SELECT CI.Name as name, CO.Name as country, CI.Population as population FROM city CI LEFT JOIN country CO ON CI.countrycode = CO.code WHERE CI.id = CO.capital AND CO.continent = 'Europe' ORDER BY CI.population DESC;",
                        "All the capital cities in a continent organised by largest population to smallest.")
        ));

        PopulationReports = new ArrayList<Query>(Arrays.asList(new Query(
                "SELECT CO.continent as name, SUM(distinct CO.Population) AS total_pop, (SUM(distinct CI.Population) / SUM(distinct CO.population)) AS urban_pop_percentage, (1 - SUM(distinct CI.Population) / SUM(distinct CO.Population)) AS rural_pop_percentage FROM country CO LEFT JOIN city CI ON CO.code = CI.countrycode GROUP BY CO.Continent ORDER By SUM(CO.Population) DESC;",
                "The population of people, people living in cities, and people not living in cities in each continent.")
        ));
    }
}
