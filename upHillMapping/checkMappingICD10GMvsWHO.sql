WITH
    diff_1 AS
    (   SELECT
            concept_code AS diff_1_cc
        FROM
            dev_cdm.concept
        
        EXCEPT
        
        SELECT
            concept_code
        FROM
            dev_cdm.conceptwho
    )
    ,
    diff_2 AS
    (   SELECT
            diff_1_cc,
            CASE
                WHEN LENGTH(diff_1_cc)=3
                THEN diff_1_cc
                WHEN LENGTH(diff_1_cc)=5
                THEN LEFT(diff_1_cc,-2)
                WHEN LENGTH(diff_1_cc)=6
                THEN LEFT(diff_1_cc,-1)
            END AS diff_2_cc
        FROM
            diff_1
    )
    ,
    diff_3 AS
    (   SELECT
            diff_1_cc,
            diff_2_cc,
            concept_id,
            concept_name,
            CASE
                WHEN LENGTH(diff_2_cc)=5
                THEN
                    CASE
                        WHEN concept_id IS NULL
                        THEN LEFT(diff_2_cc,-2)
                        ELSE NULL
                    END
                ELSE NULL
            END AS diff_3_cc
        FROM
            diff_2
        LEFT JOIN
            conceptwho
        ON
            diff_2.diff_2_cc=conceptwho.concept_code
    )
    ,
    diff_4 AS
    (   SELECT
            diff_3.*,
            conceptwho.concept_name AS diff_4_cn_who
        FROM
            diff_3
        LEFT JOIN
            conceptwho
        ON
            diff_3.diff_3_cc=conceptwho.concept_code
    )
    ,
    fin AS
    (   SELECT
            concept.concept_name AS original_concept_name_gm,
            diff_4.*
        FROM
            diff_4
        JOIN
            concept
        ON
            concept.concept_code=diff_4.diff_1_cc
    )
SELECT DISTINCT
    *
FROM
    fin
WHERE
    1=1
    --AND diff_4_cn IS NULL
    --AND diff_3_cc IS NOT NULL