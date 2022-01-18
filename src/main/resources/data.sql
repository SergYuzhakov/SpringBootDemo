DELETE FROM TODO;
INSERT INTO TODO (id, description, created, modified, completed)
VALUES ( '1000', 'New Describe 1', '2020-01-30 10:00:00', '2020-01-30 13:00:00', false ),
       ( '1001', 'New Describe 2', '2020-01-23 10:25:00', '2020-01-30 13:00:00', false ),
       ( '1002', 'New Describe 3', '2020-01-25 10:15:00', '2020-01-30 13:00:00', false ),
       ( '1003', 'New Describe 4', '2020-01-13 12:00:00', '2020-01-30 13:00:00', false ),
       ( '1004', 'New Describe 5', '2020-01-06 13:00:00', '2020-01-30 13:00:00', false ),
       ( '1005', 'New Describe 6', '2020-01-01 10:00:00', '2020-01-30 13:00:00', false )