import pandas as pd
from sqlalchemy import create_engine


server   = 'server_name'
database = 'HDI_WDI_WHR'
driver   = 'ODBC Driver 17 for SQL Server'

if server == 'server_name':
    print('Please set the server name in the script.')
    exit()

connection_string = (
    f"mssql+pyodbc://@{server}/{database}"
    f"?driver={driver.replace(' ', '+')}"
    "&trusted_connection=yes"
)

engine = create_engine(connection_string, fast_executemany=True)

# test data
data_dict = 'Data/'
df_hdi = pd.read_csv(data_dict + 'hdi_long.csv', sep=";")
df_wdi = pd.read_csv(data_dict + 'wdi_long.csv', sep=";")
df_whr = pd.read_csv(data_dict + 'whr_long.csv', sep=";")


test_number = input('Podaj numer testu (1-4): ')

if test_number == '1':
    queries = {
        "f_hdi_country_fk": """
            SELECT f.country_key AS missing_key, COUNT(*) AS orphan_count
            FROM f_hdi AS f
            LEFT JOIN dim_country AS d ON f.country_key = d.country_key
            WHERE d.country_key IS NULL
            GROUP BY f.country_key;
        """,
        "f_economic_country_fk": """
            SELECT f.country_key AS missing_key, COUNT(*) AS orphan_count
            FROM f_economic AS f
            LEFT JOIN dim_country AS d ON f.country_key = d.country_key
            WHERE d.country_key IS NULL
            GROUP BY f.country_key;
        """,
        "f_happiness_country_fk": """
            SELECT f.country_key AS missing_key, COUNT(*) AS orphan_count
            FROM f_happiness AS f
            LEFT JOIN dim_country AS d ON f.country_key = d.country_key
            WHERE d.country_key IS NULL
            GROUP BY f.country_key;
        """,
        "f_hdi_date_fk": """
            SELECT f.date_key AS missing_key, COUNT(*) AS orphan_count
            FROM f_hdi AS f
            LEFT JOIN dim_year AS d ON f.date_key = d.year_key
            WHERE d.year_key IS NULL
            GROUP BY f.date_key;
        """,
        "f_economic_date_fk": """
            SELECT f.date_key AS missing_key, COUNT(*) AS orphan_count
            FROM f_economic AS f
            LEFT JOIN dim_year AS d ON f.date_key = d.year_key
            WHERE d.year_key IS NULL
            GROUP BY f.date_key;
        """,
        "f_happiness_date_fk": """
            SELECT f.date_key AS missing_key, COUNT(*) AS orphan_count
            FROM f_happiness AS f
            LEFT JOIN dim_year AS d ON f.date_key = d.year_key
            WHERE d.year_key IS NULL
            GROUP BY f.date_key;
        """
    }

    print('\nTesty poprawności kluczy obcych w tabelach faktów:')
    for test_name, sql in queries.items():
        df = pd.read_sql(sql, engine)
        print(f"{test_name}: {len(df) == 0}")

if test_number == '2':
    queries = {
        "dim_year_unique": """
            SELECT year_key AS key_value, COUNT(*) AS count_keys
            FROM dbo.dim_year
            GROUP BY year_key
            HAVING COUNT(*) > 1;
        """,
        "dim_country_unique": """
            SELECT country_key AS key_value, COUNT(*) AS count_keys
            FROM dbo.dim_country
            GROUP BY country_key
            HAVING COUNT(*) > 1;
        """
    }

    print('\nTesty poprawności kluczy głównych w tabelach wymiarów:')
    for test_name, sql in queries.items():
        df = pd.read_sql(sql, engine)
        print(f"{test_name}: {len(df) == 0}")

if test_number == '3':
    queries = {
        "dim_year_nulls": """
            SELECT COUNT(*) AS null_count
            FROM dbo.dim_year
            WHERE year_key IS NULL;
        """,
        "dim_country_nulls": """
            SELECT COUNT(*) AS null_count
            FROM dbo.dim_country
            WHERE country_key IS NULL;
        """
    }

    print('\nTesty poprawności wartości NULL w tabelach wymiarów:')
    for test_name, sql in queries.items():
        df = pd.read_sql(sql, engine)
        print(f"{test_name}: {df['null_count'].iloc[0] == 0}")

if test_number == '4':
    queries = {
        "f_whr_corruption": """
            select dc.country_name, sum(fh.corruption) as sum
            from f_happiness as fh join dim_country as dc on dc.country_key = fh.country_key
            group by fh.country_key, dc.country_name
            order by dc.country_name;
        """,
        "f_wdi_gini_idx": """
            select dc.country_name, sum(gini_index) as sum 
            from f_economic as fe join dim_country as dc on fe.country_key = dc.country_key 
            group by fe.country_key, dc.country_name
            order by dc.country_name
        """,
        "f_hdi_population": """
        select dc.iso3_code, sum(fh.population) as sum
        from f_hdi as fh join dim_country as dc on fh.country_key = dc.country_key 
        group by fh.country_key, dc.iso3_code
        order by dc.iso3_code
        """
    }

    df_whr_sum = df_whr[
        df_whr['metric'] == 'Explained by: Perceptions of corruption'].groupby(['country name']).agg({'value': 'sum'}).reset_index()
    df_wdi_sum = df_wdi[df_wdi['metric code'] == 'SI.POV.GINI'].groupby(['country name']).agg({'value': 'sum'}).reset_index()
    df_hdi_sum = (df_hdi[df_hdi['metric'] == 'pop_total']
                  .groupby(['iso3']).
                  agg({'value': 'sum'}).reset_index())

    print('\nTesty poprawności sumarycznych wartości w tabelach faktów:')
    for test_name, sql in queries.items():
        df = pd.read_sql(sql, engine)

        if test_name == "f_whr_corruption":
            result = df.merge(df_whr_sum, left_on='country_name', right_on='country name', how='left', suffixes=('', '_sum'))
            result['difference'] = (result['sum'] - result['value']).abs()
            result = (result['difference'] < 1e-4).all()
            print(f"{test_name}: {result}")
        elif test_name == "f_wdi_gini_idx":
            result = df.merge(df_wdi_sum, left_on='country_name', right_on='country name', how='left', suffixes=('', '_sum'))
            result['difference'] = (result['sum'] - result['value']).abs()
            result = result[result['difference'].notnull()]
            result['difference'].to_csv('gini_difference.csv', index=False)
            result = (result['difference'] < 1e-4).all()
            print(f"{test_name}: {result}")
        elif test_name == "f_hdi_population":
            result = df.merge(df_hdi_sum, left_on='iso3_code', right_on='iso3', how='left', suffixes=('', '_sum'))
            result['difference'] = (result['sum'] - result['value']).abs()
            result = result[result['difference'].notnull()]
            result['difference'].to_csv('hdi_difference.csv', index=False)
            result = (result['difference'] < 1e-4).all()
            print(f"{test_name}: {result}")
