DROP TABLE IF EXISTS identitystore_client_code_task_right;
CREATE TABLE identitystore_client_code_task_right
(
    client_code_source      VARCHAR(255) NOT NULL,
    client_code_task_user   VARCHAR(255) NOT NULL,
    asked_rights            VARCHAR(50)  NOT NULL,
    CONSTRAINT identitystore_client_code_task_right_pkey PRIMARY KEY (client_code_source, client_code_task_user, asked_rights)
);
