INSERT INTO tb_type_element (name) SELECT 'Identity' WHERE NOT EXISTS (SELECT 1 FROM tb_type_element WHERE name = 'Identity');
INSERT INTO tb_type_element (name) SELECT 'Login' WHERE NOT EXISTS (SELECT 1 FROM tb_type_element WHERE name = 'Login');
INSERT INTO tb_type_element (name) SELECT 'Credit Card' WHERE NOT EXISTS (SELECT 1 FROM tb_type_element WHERE name = 'Credit Card');