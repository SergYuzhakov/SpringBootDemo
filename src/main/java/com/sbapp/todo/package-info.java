@GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(
                        name = "sequence_name",
                        value = "JPWH_SEQUENCE"
                ),
                @Parameter(
                        name = "initial_value",
                        value = "1000"
                ),
                @Parameter(
                        name = "increment_size",
                        value = "1")
        })

package com.sbapp.todo;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;