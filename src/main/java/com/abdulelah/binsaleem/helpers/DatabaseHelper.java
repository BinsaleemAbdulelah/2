package com.abdulelah.binsaleem.helpers;

import com.abdulelah.binsaleem.models.reports.Report;
import com.abdulelah.binsaleem.mappers.reports.CapitalCityReportRowMapper;
import com.abdulelah.binsaleem.mappers.reports.QueryHeaderMapper;
import com.abdulelah.binsaleem.mappers.reports.PopulationReportRowMapper;
import com.abdulelah.binsaleem.mappers.reports.CityReportRowMapper;
import com.abdulelah.binsaleem.mappers.reports.CountryReportRowMapper;
import com.abdulelah.binsaleem.models.enums.ReportType;
import com.abdulelah.binsaleem.models.raw_data.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    //Run an SQL select query on the connection provided and return the ResultSet
    public static ResultSet RunSelectQuery(Connection con, String queryString) {
        ResultSet rset = null;
        try {
            Statement stmt = con.createStatement();
            rset = stmt.executeQuery(queryString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get result set");
        }
        return rset;
    }

    //Takes a DB connection object, a list of queries and a pre-defined query type
    //Executes each query and generates Report objects for each query ran
    public static List<Report> GenerateReportsForQueryArray(Connection conn, List<Query> queryArray, ReportType reportType) {

        List<Report> generatedReports = new ArrayList<Report>();

        for (Query selectQuery : queryArray) {
            System.out.println("Running query: " + selectQuery.query);
            ResultSet queryResult = DatabaseHelper.RunSelectQuery(conn, selectQuery.query);

            try {
                List<String> queryHeaders = QueryHeaderMapper.GenerateHeadersFromResultSet(queryResult);

                List reportRowList = new ArrayList();

                switch (reportType) {
                    case Country:
                        reportRowList = CountryReportRowMapper.GenerateCountryReportRowsFromResultSet(queryResult);
                        break;
                    case City:
                        reportRowList = CityReportRowMapper.GenerateCityFromResultSet(queryResult);
                        break;
                    case CapitalCity:
                        reportRowList = CapitalCityReportRowMapper.GenerateCapitalCityFromResultSet(queryResult);
                        break;
                    case Population:
                        reportRowList = PopulationReportRowMapper.GeneratePopulationReportFromResultSet(queryResult);
                        break;

                    //
                }
                generatedReports.add(new Report(selectQuery.title, reportRowList, queryHeaders, reportType));
            } catch (Exception ex) {
                System.out.println(ex);
            }
            System.out.println("Query finished: " + selectQuery.query);
        }

        return generatedReports;
    }
}
